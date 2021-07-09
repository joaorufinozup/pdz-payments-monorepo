package com.example.payments

import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

/**
 * @author Finzi
 */

interface PaymentRepository: MongoRepository<Payment, String> {

}