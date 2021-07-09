package com.payments.wallet.repositories

import com.payments.wallet.models.Wallet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface WalletRepository: JpaRepository<Wallet, UUID> {
    fun findByCustomerId(id: UUID): Optional<Wallet>
}