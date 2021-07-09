package com.example.payments

import java.time.LocalDateTime

/**
 * @author Finzi
 */

data class Payment(
    var id: String?,
    var id_customer: String,
    var id_subscription: String,
    var value: Float,
    var processing_date: String? = LocalDateTime.now().toString(),
    var status: Int? = 0
    ) {
}