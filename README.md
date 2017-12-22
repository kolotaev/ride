# Ride

[![Build Status](https://travis-ci.org/kolotaev/ride.svg?branch=master)](https://travis-ci.org/kolotaev/ride)
[![codecov.io](https://codecov.io/github/kolotaev/ride/coverage.svg?branch=master)](https://codecov.io/github/kolotaev/ride?branch=master)

Scala global unique identifier (GUID) generator.


## Description

Ride uses Mongo Object ID algorithm to generate globally unique IDs with a custom base32 serialization to make it
shorter when transported as a string. See [docs](https://docs.mongodb.org/manual/reference/object-id).

Internally it consists of:

- first 4 bytes representing the seconds since the Unix epoch
- next 3 bytes are the current machine identifier (hostname)
- next 2 bytes are process ID
- the last 3 bytes are counter, starting with a random value.

The binary representation of the ID is compatible with Mongo 12 bytes Object IDs.
The string representation is using base32 hex (w/o padding) for better space efficiency
when stored in that form (20 bytes). The hex variant of base32 is used to retain the
sortable property of the ID.


## Features

- Size: 12 bytes (96 bits), smaller than UUID, larger than snowflake
- Base32 hex encoded by default (20 chars when transported as printable string, still sortable)
- Non configured, you don't need to set up a unique machine and/or data center ID
- K-ordered
- Embedded time with 1 second precision
- Unicity guaranteed for 16,777,216 (24 bits) unique IDs per second and per host/process
- Lock-free


## Usage

Generating IDs:

```scala
import org.kolotaev.ride.Id

val guid = Id()

println(guid)
// String => b8uhqvioith6uqnvvvq0

guid.getBytes
// => Array[Byte] = Array(90, 61, 29, 126, 88, -105, 98, 111, 106, -1, -1, -12)
// Byte representation of ID

val ids = Array.fill[Id](3) { Id() }
// Array(b8ui8kioith721fvvvj0, b8ui8kioith721fvvvjg, b8ui8kioith721fvvvk0)
```

Reproducing IDs:

```scala
val guid2 = Id("b8uhqvioith6uqnvvvq0")

println(s"$guid" == s"$guid2")
// guid and guid2 are the same strings

// Creating ID from malformed string IllegalArgumentException throws an exception
val guid3 = Id("bad-string")
```

Obtaining embedded info:

```scala
guid.time
// => java.time.LocalDateTime = 2017-12-22T17:58:06
// Local time embedded into ID

guid.pid
// => Array[Byte] = Array(88, -105, 98)
// Prints PID embedded into ID. It's a truncated version of the real PID

guid.machine
// Array[Byte] = Array(88, -105, 98)
// Stored machine identifier bytes

guid.counter
// Int = 56
```

Ride implements `Serializable` and `Ordered[T]`.


## Comparison with other unique identifiers

UUIDs are 16 bytes (128 bits) and 36 chars as string representation. Twitter Snowflake
IDs are 8 bytes (64 bits) but require machine/data-center configuration and/or central
generator servers. Ride stands in between with 12 bytes (96 bits) and a more compact
URL-safe string representation (20 chars). No configuration or central generator server
is required so it can be used directly in server's code.

| Name        | Binary Size | String Size    | Features
|-------------|-------------|----------------|----------------
| _UUID_      | 16 bytes    | 36 chars       | configuration free, not sortable
| _Snowflake_ | 8 bytes     | up to 20 chars | needs machine/data-center configuration, needs central server, sortable
| _MongoID_   | 12 bytes    | 24 chars       | configuration free, sortable
| _Ride_      | 12 bytes    | 20 chars       | configuration free, sortable


## License

The source code is licensed under the [MIT License](https://raw.github.com/kolotaev/ride/master/LICENSE).
