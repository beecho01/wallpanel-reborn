/*
 * Copyright (c) 2022 WallPanel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed 
 * under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package io.github.beecho01.wallpanelreborn.ui.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import io.github.beecho01.wallpanelreborn.R
import io.github.beecho01.wallpanelreborn.ui.activities.SettingsActivity
import io.github.beecho01.wallpanelreborn.databinding.FragmentAboutBinding

import timber.log.Timber

class AboutFragment : Fragment() {


    private lateinit var binding: FragmentAboutBinding
    private var versionNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAboutBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Set title bar
        if((activity as SettingsActivity).supportActionBar != null) {
            (activity as SettingsActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            (activity as SettingsActivity).supportActionBar!!.setDisplayShowHomeEnabled(true)
            (activity as SettingsActivity).supportActionBar!!.title = (getString(R.string.pref_about_title))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            view?.let { Navigation.findNavController(it).navigate(R.id.settings_action) }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            val packageInfo = requireActivity().packageManager.getPackageInfo(requireActivity().packageName, 0)
            versionNumber = " v" + packageInfo.versionName
            binding.versionName.text = versionNumber
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.e(e.message)
        }

        binding.sendFeedbackButton.setOnClickListener { feedback() }
        // No store listing yet, so hide the rating entry.
        binding.rateApplicationButton.visibility = View.GONE
        binding.githubButton.setOnClickListener { showGitHub() }
        binding.supportButton.setOnClickListener { showSupport() }
        binding.privacyPolicyButton.setOnClickListener { showPrivacyPolicy() }
    }

    private fun showSupport() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(SUPPORT_URL)))
    }

    private fun showGitHub() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(GITHUB_URL)))
    }

    private fun showPrivacyPolicy() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_POLICY_URL)))
    }

    private fun feedback() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(ISSUES_URL)))
    }

    companion object {
        const val SUPPORT_URL:String = "https://beecho01.github.io/wallpanel-reborn"
        const val GITHUB_URL = "https://github.com/beecho01/wallpanel-reborn"
        const val ISSUES_URL = "https://github.com/beecho01/wallpanel-reborn/issues"
        const val PRIVACY_POLICY_URL = "https://beecho01.github.io/wallpanel-reborn/privacy-policy"

        fun newInstance(): AboutFragment {
            return AboutFragment()
        }
    }
}