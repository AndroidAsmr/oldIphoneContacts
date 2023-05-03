package com.hadirahimi.oldiphonecontacts.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hadirahimi.oldiphonecontacts.databinding.ItemContactsBinding
import com.hadirahimi.oldiphonecontacts.model.ContactInfo
import java.lang.Exception

class AdapterContact : RecyclerView.Adapter<AdapterContact.MyViewHolder>()
{
    private lateinit var binding : ItemContactsBinding
    private var list = emptyList<ContactInfo>()
    private lateinit var context : Context
    private var lastChar = ""
    inner class MyViewHolder:RecyclerView.ViewHolder(binding.root)
    {
        fun setData(contact:ContactInfo)
        {
            binding.apply {
                tvChar.text = contact.name.first().toString()
                tvContactName.text = contact.name
                
                if (contact.photoUri == null)
                {
                    try
                    {
                        val sName = contact.name.split("[^\\w']+".toRegex())
                        if (sName.size == 1)
                        {
                            tvShortName.append(sName[0].first().toString())
                        }
                        else if (sName.size>=2)
                        {
                            val shortName = "${sName[0].first()}${sName[1].first()}"
                            tvShortName.text = shortName
                        }
                    }catch (e:Exception)
                    {
                        tvShortName.text = "#"
                    }
                }
                else{
                    Glide.with(context)
                        .load(contact.photoUri)
                        .into(profileImage)
                }
            }
        }
    }
    
    override fun onCreateViewHolder(parent : ViewGroup , viewType : Int) : MyViewHolder
    {
        context = parent.context
        binding = ItemContactsBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyViewHolder()
    }
    
    override fun getItemCount() : Int = list.size
    
    override fun onBindViewHolder(holder : MyViewHolder , position : Int)
    {
        holder.setData(list[position])
        if (position == 0)
        {
            binding.tvChar.visibility = View.VISIBLE
            lastChar = binding.tvChar.text.toString()
        }
        else if (binding.tvChar.text == lastChar)
        {
            binding.tvChar.visibility = View.GONE
            lastChar = binding.tvChar.text.toString()
        }
        else
        {
            binding.tvChar.visibility = View.VISIBLE
            lastChar = binding.tvChar.text.toString()
        }
    }
    
    override fun getItemViewType(position : Int) : Int
    {
        return position
    }
    fun submitData(contactList : List<ContactInfo>)
    {
        val contactDiffUtil = ContactDiffUtils(list,contactList)
        val diffUtils = DiffUtil.calculateDiff(contactDiffUtil)
        list = contactList
        diffUtils.dispatchUpdatesTo(this)
    }
    class ContactDiffUtils(
        private val oldList:List<ContactInfo>,
        private val newList:List<ContactInfo>,
    ):DiffUtil.Callback()
    {
        override fun getOldListSize() : Int = oldList.size
    
        override fun getNewListSize() : Int = newList.size
    
        override fun areItemsTheSame(oldItemPosition : Int , newItemPosition : Int) : Boolean
        {
            return oldList[oldItemPosition] === newList[newItemPosition]
        }
    
        override fun areContentsTheSame(oldItemPosition : Int , newItemPosition : Int) : Boolean
        {
            return oldList[oldItemPosition].phone === newList[newItemPosition].phone
        }
    }
}










