# Change Log


## 1.1.1 - [2020-02-10]

Added:

- Support for Scala 2.13.

Changed:

- `Id#equals` now uses `TraversableOnce#mkString` internally for comparison
instead of `ArrayLike#deep` which is removed from Scala 2.13.
This solves previous release (1.1.0) source-incompatibility with Scala 2.13.


## 1.1.0 - [2019-07-28]

Added:

- Construction of an Id object can be done with it's byte-array representation.


## 1.0.1

Added:

- Id object equality.



## 1.0.0

Added:

- Initial release of Ride.
