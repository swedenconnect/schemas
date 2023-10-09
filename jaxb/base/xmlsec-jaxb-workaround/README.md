![Logo](https://docs.swedenconnect.se/schemas/img/sweden-connect.png)

------

# Workaround for JAXB and Apache XML Security


Apache XML Security (org.apache.santurio:xmlsec) contains Java classes for XMLDSig and XMLEnc, but the generated
episode file (sun-jaxb.episode) is missing the `if-exists` attribute which triggers the
"SCD x-schema::tns didnt match any schema component"-bug (see https://github.com/highsource/maven-jaxb2-plugin/issues/41).

Therefore, we deliver an empty jar with its only purpose of delivering a patched sun-jaxb.episode file.

This jar will only be used during the build/generation phase.


Copyright &copy; 2017-2022, [Sweden Connect](https://swedenconnect.se). Licensed under version 2.0 of the [Apache License](http://www.apache.org/licenses/LICENSE-2.0).