package com.example.openbanking.data

import com.fasterxml.jackson.annotation.JsonProperty

data class AccountRoot(
    @JsonProperty("Data")
    val data: Data,
) {
    data class Data(
        @JsonProperty("Account")
        val account: List<Account>,
    )
}

data class Account(
    val accountId: String,
    val status: String,
    val statusUpdateDateTime: String,
    val currency: String,
    val accountType: String,
    val accountDescription: String,
    @field:JsonProperty("AccountDetails") val accountDetails: List<AccountDetail>,
    @field:JsonProperty("Owner") val owner: Owner,
    @field:JsonProperty("Servicer") val servicer: Servicer
) {

    data class AccountDetail(val name: String,
    val schemeName: String,
    val identification: String)

    data class Owner(
        val name: String,
        val mobileNumber: String,
        val countryOfResidence: String,
        val countryOfBirth: String,
        val provinceOfBirth: String,
        val cityOfBirth: String,
        val birthDate: String,
        @field:JsonProperty("Identification") val identification: List<Identification>,
        @field:JsonProperty("PostalAddress") val postalAddress: PostalAddress
    )

    data class Identification(val schemeName: String,
        val identification: String)

    data class PostalAddress(
        val addressType: String,
        val addressLine: List<String>,
        val streetName: String,
        val buildingNumber: String,
        val postCode: String,
        val townName: String,
        val countrySubDivision: String,
        val country: String
    )

    data class Servicer(
        val name: String,
        @field:JsonProperty("BankIdentification") val bankIdentification: List<BankIdentification>,
        @field:JsonProperty("OrganizationIdentification") val organizationIdentification: List<OrganizationIdentification>,
        @field:JsonProperty("CorrespondentAccount") val correspondentAccount: CorrespondentAccount,
        @field:JsonProperty("PostalAddress") val postalAddress: PostalAddress2
    )

    data class BankIdentification(
        val schemeName: String,
        val identification: String
    )

    data class OrganizationIdentification(
        val schemeName: String,
        val identification: String
    )

    data class CorrespondentAccount(
        val schemeName: String,
        val identification: String
    )

    data class PostalAddress2(val addressLine: List<String>)

}