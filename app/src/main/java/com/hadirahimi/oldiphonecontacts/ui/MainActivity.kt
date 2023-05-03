package com.hadirahimi.oldiphonecontacts.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Adapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.hadirahimi.oldiphonecontacts.R
import com.hadirahimi.oldiphonecontacts.databinding.ActivityMainBinding
import com.hadirahimi.oldiphonecontacts.model.ContactInfo
import com.hadirahimi.oldiphonecontacts.ui.adapter.AdapterContact

class MainActivity : AppCompatActivity()
{
    private lateinit var binding : ActivityMainBinding
    private lateinit var adapterContact : AdapterContact
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //adapter
        adapterContact = AdapterContact()
        
        //check permission
        if (checkPermission())
        {
            setupRecycler()
        }else requestPermission()
    }
    
    private fun requestPermission() = ActivityCompat.requestPermissions(this@MainActivity,
    arrayOf(Manifest.permission.READ_CONTACTS),
        100)
    
    private fun setupRecycler()
    {
        binding.recyclerviewContacts.apply {
            layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.VERTICAL,false)
            adapter = adapterContact
        }
        adapterContact.submitData(getContactList())
    }
    
    private fun checkPermission():Boolean =
        ActivityCompat.checkSelfPermission(this@MainActivity,Manifest.permission.READ_CONTACTS)==PackageManager.PERMISSION_GRANTED

    private fun getContactList():ArrayList<ContactInfo>
    {
        val contactList = ArrayList<ContactInfo>()
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                val name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))?:""
                val phone = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))?:""
                val photoUri = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
                contactList.add(ContactInfo(name,phone,photoUri))
            }
            cursor.close()
        }
        return contactList
    }
    
    override fun onRequestPermissionsResult(
        requestCode : Int ,
        permissions : Array<out String> ,
        grantResults : IntArray
    )
    {
        super.onRequestPermissionsResult(requestCode , permissions , grantResults)
        if (requestCode == 100)
        {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                //permission granted
                setupRecycler()
            }else
            {
                //permission is not granted show a message
                Toast.makeText(this@MainActivity , "Permission denied" , Toast.LENGTH_SHORT).show()
            }
        }
    }


}








