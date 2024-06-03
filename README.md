# Bali BOM

**WARNING:** Work in Progress

Bill Of Material (BOM) management software for PCB micro-factories.

This software is intended for people who design and manufacture PCB,
including using pick-and-place machines.

## Motivation
When you start manufacturing electronics boards beyond a few hobby 
boards, keeping track of which components should be ordered from
which supplier and when, becomes a major concern and occupying more
time than necessary.

Most part/component suppliers have APIs available, where one can
obtain all necessary details such as price, stock and delivery times
if not in stock.

## Focus
This software is not intended for large contract manufacturers, but
those who make (for themselves and/or others) up to a few dozen boards
and perhaps up to 1000 or so different parts/components.

It is a desktop application intended to be used by a single person,
or at the most running on a single computer with trusted users accessing
it. There are no security mechanisms whatsoever. And data is stored
on the local hard disk in a JSON-per-entity format, allowing for 
integration with other tools relatively easy, without having to stand up
a full database.

## Workflow
  1. During design create the following columns to be included in the BOM;
     1. MF (Manufacturer's Name)
     1. MPN (Manufacturer's Part Number)
     1. DigiKey, Digi-Key, DigiKeyPN, Digi-Key_PN or Digi-KeyPN
     1. Mouser, Mouser-PN, Mouser_PN or MouserPN
     1. LCSC

  1. Name the BOM file; \[product name]-Rev\[revision]-bom.csv

  1. In this program, under "Products" tab, click "Import BOM..." and
     select the BOM file from the file system.
     This will create an unresolved Product in the system. Unresolved
     means that the Parts have not been found yet.

  1. Select one or more Products and click on "Resolve Parts". This will
     try to locate each of the Parts from the Enabled Suppliers.

  1. "Cost Estimate..." allows you to get an idea of the total cost when
     producing a particular quantity of a Product. It is an estimate
     because only the exact amount of parts are looked up prices for,
     and not taking into account existing inventory and what prices are
     when buying larger quantities. Also, wastage is currently not 
     considered.

  1. Jobs are created under the "Jobs" tab, "Create Jobs..."