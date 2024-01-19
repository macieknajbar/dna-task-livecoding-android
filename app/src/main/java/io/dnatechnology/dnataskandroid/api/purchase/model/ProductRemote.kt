package io.dnatechnology.dnataskandroid.api.purchase.model

/**
 * productID - globally unique product identifier
 * name - display name
 * maxAmount - available quantity of the product
 * unitNetValue - net value of a single item
 * unitValueCurrency - currency name
 * tax - tax to be added to the net value
 */
data class ProductRemote(val productID: String,
                         val name: String,
                         val maxAmount: Long,
                         val unitNetValue: Double,
                         val unitValueCurrency: String,
                         val tax: Double) {
    override fun toString(): String {
        return String.format("%s [ %.2f %s ]", name, unitNetValue * (1.0+ tax), unitValueCurrency)
    }
}
