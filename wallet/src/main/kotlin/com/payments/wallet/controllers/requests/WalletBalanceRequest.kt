package com.payments.wallet.controllers.requests

import java.util.*

data class WalletBalanceRequest(
    val customerId: UUID,
    val balance: Float
)
