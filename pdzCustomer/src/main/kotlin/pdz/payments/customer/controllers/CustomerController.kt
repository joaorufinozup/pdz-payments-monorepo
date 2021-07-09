package pdz.payments.customer.controllers

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import pdz.payments.customer.models.Customer
import pdz.payments.customer.repositories.CustomerRepository
import java.util.*

@RestController
@RequestMapping("/customer")
class CustomerController(val repository: CustomerRepository) {
    val endpointWallet = "http://wallet:8080/wallet"

    @GetMapping
    fun findAll(): MutableIterable<Customer> = repository.findAll()

    @PostMapping
    fun addCustomer(@Validated @RequestBody request: Customer): ResponseEntity<Customer>{
        val response = repository.save(request)
        return ResponseEntity(response,HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun updateCustomer(@PathVariable id: UUID, @RequestBody request: Customer) {
        val customer = repository.findById(id)
        if (customer.isPresent)
        //assert(customer.id == id)
        repository.save(request)
    }

    @DeleteMapping("/{id}")
    fun removeCustomer(@PathVariable id: UUID) {
       repository.deleteById(id)
    }

    @GetMapping("/{cpf}/balance")
    fun getBalance(@PathVariable cpf: String): ResponseEntity<Float> {
        val customer = repository.findByCustomerCpf(cpf)
        if(customer.isPresent) {
            val client = OkHttpClient()
            val request: Request = Request.Builder()
                .url("$endpointWallet/balance/${customer.get().id}")
                .build()

            val response: Response = client.newCall(request).execute()

            return ResponseEntity.ok(response.body()!!.string().toFloat())
        }

        return ResponseEntity(HttpStatus.NOT_FOUND)
    }
}