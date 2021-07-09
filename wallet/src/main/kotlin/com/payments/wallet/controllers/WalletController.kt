package com.payments.wallet.controllers

import com.payments.wallet.controllers.requests.WalletBalanceRequest
import com.payments.wallet.controllers.requests.WalletPostRequest
import com.payments.wallet.controllers.requests.WalletPutRequest
import com.payments.wallet.models.Wallet
import com.payments.wallet.repositories.WalletRepository
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.Duration
import java.util.*

@RestController
@RequestMapping("/wallet")
class WalletController(val walletRepository: WalletRepository) {
    @GetMapping
    fun index(): ResponseEntity<List<Wallet>> {
        val wallets = walletRepository.findAll()
        return ResponseEntity.ok(wallets);
    }

    @GetMapping("/{id}")
    fun show(@PathVariable id: UUID): ResponseEntity<Wallet> {
        val wallet = walletRepository.findById(id)
        if (wallet.isPresent) {
            return ResponseEntity.ok(wallet.get())
        }
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PostMapping
    fun create(@Validated @RequestBody request: WalletPostRequest): ResponseEntity<Wallet> {
        val wallet = Wallet(customerId = request.customerId, balance = request.balance, dateLastProcessing = request.dateLastProcessing)
        val response = walletRepository.save(wallet)
        return ResponseEntity(response, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun update(@Validated @RequestBody request: WalletPutRequest, @PathVariable id: UUID): ResponseEntity<Wallet> {
        val wallet = walletRepository.findById(id)
        if (wallet.isPresent) {
            wallet.get().balance = request.balance
            wallet.get().dateLastProcessing = request.dateLastProcessing
            walletRepository.save(wallet.get())
            return ResponseEntity.ok(wallet.get())
        }
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<String> {
        val wallet = walletRepository.findById(id)
        if (wallet.isPresent) {
            walletRepository.deleteById(id)
            return ResponseEntity(HttpStatus.OK)
        }
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PostMapping("/add-balance")
    fun addBalance(@Validated @RequestBody request: WalletBalanceRequest): ResponseEntity<String> {
        val wallet = walletRepository.findByCustomerId(request.customerId)
        if (wallet.isPresent) {
            wallet.get().balance = wallet.get().balance + request.balance
            wallet.get().dateLastProcessing = Date().toString()
            walletRepository.save(wallet.get())
            return ResponseEntity.ok("OK")
        }

        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PostMapping("/debit-balance")
    fun debitBalance(@Validated @RequestBody request: WalletBalanceRequest): ResponseEntity<String> {
        val wallet = walletRepository.findByCustomerId(request.customerId)
        if (wallet.isPresent) {
            if (wallet.get().balance < request.balance) {
                return ResponseEntity("Insufficient balance", HttpStatus.BAD_REQUEST)
            }

            wallet.get().balance = wallet.get().balance - request.balance
            wallet.get().dateLastProcessing = Date().toString()
            walletRepository.save(wallet.get())
            return ResponseEntity.ok("OK")
        }

        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @GetMapping("/balance/{customerId}")
    fun getBalance(@PathVariable customerId: UUID): ResponseEntity<Float> {
        val wallet = walletRepository.findByCustomerId(customerId)
        if (wallet.isPresent) {
            return ResponseEntity.ok(wallet.get().balance)
        }

        return ResponseEntity(HttpStatus.NOT_FOUND)
    }
}