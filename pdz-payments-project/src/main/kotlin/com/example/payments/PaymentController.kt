package com.example.payments

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import okhttp3.*
import org.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestBody


import java.util.*

/**
 * @author Finzi
 */
@RestController
@RequestMapping("payment")
class PaymentController(val repository: PaymentRepository) {
    ///debit-balance
    val endpointPayments = "https://run.mocky.io/v3/5aade899-0865-43b8-9bd2-86b29ed34902"

    @PostMapping
    fun create(@RequestBody payment: Payment): ResponseEntity<String> {


        try {
            val obj = JSONObject()

            obj.put("customerId", payment.id_customer) //uuid
            obj.put("balance", payment.value) //float

            val client = OkHttpClient()
            val request: Request = Request.Builder()
                .url(endpointPayments)
                .build()

            val response: Response = client.newCall(request).execute()
            if (response.code() == 200) {
                payment.status = 1
                repository.save(payment)
                return ResponseEntity(
                    "Pagamento realizado com sucesso.",
                    HttpStatus.OK);
            } else if(response.code() == 400) {
                repository.save(payment)
                return ResponseEntity(
                        "Não foi possível realizar o pagamento.",
                HttpStatus.BAD_REQUEST);
            } else {
                repository.save(payment)
                return ResponseEntity(
                    "Ocorreu um erro ao processar seu pagamento.",
                    HttpStatus.OK);
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return ResponseEntity(
                "Erro",
                HttpStatus.BAD_REQUEST);
        }


    }

    @GetMapping("{id}")
    fun read(@PathVariable id: String) = ResponseEntity.ok(repository.findById(id))

    @PutMapping("{id}")
    fun update(@PathVariable id: String, @RequestBody payment: Payment): ResponseEntity<Payment> {
        val paymentDBOptional = repository.findById(id)
        val paymentDB = paymentDBOptional
            .orElseThrow{RuntimeException("Payment: $id not found")}
            .copy(status = payment.status)
        return  ResponseEntity.ok(paymentDB)
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: String) = repository
        .findById(id)
        .ifPresent{ repository.delete(it)}
}