package com.payments.wallet.controllers.requests

data class WalletPutRequest(
    val balance: Float,
    val dateLastProcessing: String
)
