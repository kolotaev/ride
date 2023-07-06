# How to release

- Make sure you have a valid account in [Sonatype Nexus](https://oss.sonatype.org)
- Make sure you have Nexus account credentials in `~/.sbt/$SBT_VERSION/sonatype.sbt`
    Example:
    ```scala
    credentials += Credentials(Path.userHome / ".sbt" / "sonatype_credentials")
    ```
    And a protected file with credentials:
    ```
    realm=Sonatype Nexus Repository Manager
    host=oss.sonatype.org
    user=<your username>
    password=<your password>
    ```
- List GPG keys with `sbt pgp-cmd list-keys`
- Check the presence of key for this library release (it's called `ride`). If not found: generate a new one with `sbt pgp-cmd gen-key` and then post it to GPG ubuntu keyserver with `sbt pgp-cmd send-key ride http://keyserver.ubuntu.com:11371`
- Make sure tests are passing (locally and on CI)
- Create a release at the Github
- Checkout released tag locally
- Publish to staging with `sbt publishSigned`
- Promote to Maven Central with `sbt sonatypeRelease`
- Test that the library is available in Maven Central
- Repeat for all ride supported Scala versions
