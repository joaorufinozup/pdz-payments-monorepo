package com.payments.wallet.models

import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "wallet")
data class Wallet(
    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(name = "customer_id", nullable = false)
    val customerId: UUID,

    @Column(name = "balance", nullable = false)
    var balance: Float,

    @Column(name = "date_last_processing", nullable = false)
    var dateLastProcessing: String
)
