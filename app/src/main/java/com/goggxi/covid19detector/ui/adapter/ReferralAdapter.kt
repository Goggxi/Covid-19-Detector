package com.goggxi.covid19detector.ui.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.goggxi.covid19detector.data.model.Referral
import com.goggxi.covid19detector.databinding.ItemRowReferralBinding


class ReferralAdapter(private val listReferral: List<Referral?>?) : RecyclerView.Adapter<ReferralAdapter.ListViewHolder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = ItemRowReferralBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        listReferral?.get(position)?.let { holder.bind(it) }
        context = holder.itemView.context
    }

    override fun getItemCount(): Int = listReferral?.size as Int

    inner class ListViewHolder(private val binding: ItemRowReferralBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(referral: Referral) {
            with(binding){
                textTitleReferral.text = referral.name
                textAddressReferralContent.text = referral.address
                textRegionReferralContent.text = referral.region
                textTelephoneReferralContent.text = referral.phone
                textProvinceReferralContent.text = referral.province

                btnCall.setOnClickListener {
//                    Toast.makeText(context, referral.name, Toast.LENGTH_SHORT).show()
                    val phone = "+${referral.phone}"
                    val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
                    context.startActivity(intent)
                }
            }
        }
    }
}