package pdz.payments.customer.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import pdz.payments.customer.models.Customer
import java.util.*

@Repository
interface CustomerRepository: CrudRepository<Customer, UUID> {
    fun findByCustomerCpf(cpf: String): Optional<Customer>
}