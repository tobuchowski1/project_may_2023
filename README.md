[![CodeQL](https://github.com/tobuchowski1/project_may_2023/actions/workflows/codeql.yml/badge.svg)](https://github.com/tobuchowski1/project_may_2023/actions/workflows/codeql.yml)

This is a solution for competition `Zielona Tesla za Zielony kod` organized by ING Bank Śląski S.A.
[https://www.ing.pl/pionteching](https://www.ing.pl/pionteching)

CodeQL is used for the security vulnerabilities analysis. 

VisualVM was used to do profiling.

# running the code

## build and run web server listening on port 8080
```
./build.sh && ./run.sh
```
or alternatively you can run docker container simulating competition environment

```
docker compose up
```

## speed test
- you need to use prepared python scripts to generate testing data
```
tests/gen-atm.py
tests/gen-clan.py
tests/gen-trx.py
```
- then run following command to start a test (requires running server)
```
make stest
```



# dependencies

- [undertow](https://github.com/undertow-io/undertow) - Apache License v2
- [slf4j](https://github.com/qos-ch/slf4j/blob/master/LICENSE.txt) - MIT License
- [dsl-json](https://github.com/ngs-doo/dsl-json) - BSD License
- [Apache Commons Codec](https://commons.apache.org/proper/commons-codec/) - Apache License v2
