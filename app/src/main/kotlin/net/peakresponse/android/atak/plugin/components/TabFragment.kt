package net.peakresponse.android.atak.plugin.components

import androidx.fragment.app.Fragment

abstract class TabFragment : Fragment() {
    abstract fun getTabTitle(): String
}
