package com.example.revisionequipamiento

import android.content.pm.PackageInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_ajustes.*

class AjustesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajustes)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var pInfo: PackageInfo? = null
        pInfo = packageManager.getPackageInfo(packageName, 0)
        val versionS = pInfo!!.versionName
        version_tx.text="Versión: $versionS"
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {
        finish()
    }
}
