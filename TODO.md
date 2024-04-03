
# Planned Use-cases

## Products
* Import BOM
  * For each item in BOM
    * Add Manufacturer
    * Add Part
  * Save Product, immutable except for a Description


## Parts
* Suppliers probably needs to be supported programmatically, at least initially.
  * LCSC
  * Digi-Key
  * Mouser
  * RS-online
  * Farnell

* Action to fetch prices for all Parts from all Suppliers
  * For each Supplier
    * Fetch Availability, Price
    * Fetch all part parameters, and merge into Part.

## Parts Orders
* Create Parts Orders
  * Select N Products
  * Choose number to produce of each
  * Compute total number of parts needed
  * Subtract inventory at hand, not reserved
  * From each Supplier
    * Check Reel sizes
    * Compute "Best Value" from ordering more than needed.
    * Add a column for purchasing price
    * Add a column for for surplus value
  * Select Supplier based on price
  * Present Order and let user change supplier

* Edit Parts Order
  * Add more numbers to each product
  * Add more products
  * Change supplier for each Part
  * Upgrade Part item to more units, such as a full reel.
  * Send orders to suppliers or subset there of. 

* Merge Orders
  * Select two or more Orders and merge into a new one
  * Old ones are marked "Merged" and not part of "active" list.
