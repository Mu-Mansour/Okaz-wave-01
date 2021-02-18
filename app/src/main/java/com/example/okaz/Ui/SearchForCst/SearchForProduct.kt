package com.example.okaz.Ui.SearchForCst

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.okaz.Adapters.SearchAdapter
import com.example.okaz.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.search_for_product_fragment.view.*

@AndroidEntryPoint
class SearchForProduct : Fragment() {
val theSearchAdapter=SearchAdapter()

    private  val viewModel: SearchForProductViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val theView=inflater.inflate(R.layout.search_for_product_fragment, container, false)
        theView.editTextForSearching.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                theSearchAdapter.items.clear()
                theSearchAdapter.notifyDataSetChanged()
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {

                viewModel.theSearchJob(p0)

            }

        })

        theView.theItemsFoundrv.adapter=theSearchAdapter
        return theView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.theListToBeAdded.observe(viewLifecycleOwner, {
            it?.let {
                if (it.size > 0) {
                    theSearchAdapter.submitTheList(it)
                }
            }
        })
    }



}