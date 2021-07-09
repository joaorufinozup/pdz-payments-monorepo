package com.payments.wallet.controllers.requests

import java.util.*

data class WalletPostRequest(
    val customerId: UUID,
    val balance: Float,
    val dateLastProcessing: String
)
