# VERTX-EFFECT
## v1.0.0-RC1  ( Sun Dec 20 2020 18:37:45 GMT+0100 (Central European Standard Time) )

## Bug Fixes
  - 🐛 label, name and desc jfr http event
  ([c7342769](https://github.com/imrafaelmerino/vertx-effect/commit/c73427695b2345b2e0ca87a5dcb3fa412a408f60))

## Features
  - 🎸 vertx upgrade
  ([25d8f8e6](https://github.com/imrafaelmerino/vertx-effect/commit/25d8f8e616eb565e54307dd3e062cb651dcbdd60))
  - 🎸 save jfr file after executing tests
  ([1e7a0ab4](https://github.com/imrafaelmerino/vertx-effect/commit/1e7a0ab48f9739badc870862ed9b569517d2c823))
  - 🎸 http client and jfr integration
  ([3983b06e](https://github.com/imrafaelmerino/vertx-effect/commit/3983b06e1e141c483e31495db93aea32fc35d113))
  - 🎸 first,second,thrird,fourth,fifth tuples method
  ([be86d72f](https://github.com/imrafaelmerino/vertx-effect/commit/be86d72f671acb40f1462c1eaa263b05c8a69258))
  - 🎸 raceFirst function on ListExp
  ([26739823](https://github.com/imrafaelmerino/vertx-effect/commit/2673982331f7825ed4d4226b44b1eaff7ddedb0e))
  - 🎸 retryUntil function
  ([f0e8e413](https://github.com/imrafaelmerino/vertx-effect/commit/f0e8e4133c13285e007d0f2801e8ae11df79a904))
  - 🎸 head, tail methods on JsArrayVal
  ([cf93e6fd](https://github.com/imrafaelmerino/vertx-effect/commit/cf93e6fd0d144add62d8825ebb4860025edf08bf))


## Documentation
  - ✏️ readme retryWhile op
  ([380e2081](https://github.com/imrafaelmerino/vertx-effect/commit/380e20815a1050be4cd525153346d8e1cb6deb82))
  - ✏️ new logos
  ([f690a56b](https://github.com/imrafaelmerino/vertx-effect/commit/f690a56ba4ce0a503def06bf41766aebd5fa4e3b))
  - ✏️ logs new directories
  ([53e2ccb4](https://github.com/imrafaelmerino/vertx-effect/commit/53e2ccb4e40a1cc78bbb5396c6f6c17a6d994375))
  - ✏️ logos
  ([e4a334a0](https://github.com/imrafaelmerino/vertx-effect/commit/e4a334a01c70a55bddc1256c1d851368120a5be6))
  - ✏️ typo
  ([0d1cce65](https://github.com/imrafaelmerino/vertx-effect/commit/0d1cce654c974f4a3adf2f9bd5da9634e545e1cc))


## Refactor
  - 💡 http server and mocks
  ([fd84d626](https://github.com/imrafaelmerino/vertx-effect/commit/fd84d626c90373f90fd9bd7ffb7c4488f21bda20))
  - 💡 remove raceFirst op
  ([c33920dd](https://github.com/imrafaelmerino/vertx-effect/commit/c33920ddbdc93943b0860026b8cc1a247935e9a0))
  - 💡 retryWhile
  ([5a203dff](https://github.com/imrafaelmerino/vertx-effect/commit/5a203dff11dc6d18bbe526946aeeddb530da2eab))
  - 💡 server handler
  ([8636bea3](https://github.com/imrafaelmerino/vertx-effect/commit/8636bea337064cc10b695655f87dde410ae4ca96))
  - 💡 server response functions
  ([442198e5](https://github.com/imrafaelmerino/vertx-effect/commit/442198e50d3ffb218db2e1e640ef28e29e9074b4))
  - 💡 Failures refactor
  ([ad35752a](https://github.com/imrafaelmerino/vertx-effect/commit/ad35752ae9433d9cd6665f915f9014841cfd8e05))
  - 💡 MapExp returns java map instead of vavr map
  ([6852010f](https://github.com/imrafaelmerino/vertx-effect/commit/6852010f2b0866d13cf68a01dc975e1fb067ec19))
  - 💡 ListExp returns a java List instead of vavr list
  ([c071f80f](https://github.com/imrafaelmerino/vertx-effect/commit/c071f80fe1de99c66c266fc3b2345046f3773044))
  - 💡 CONNECTION_WAS_CLOSED_CODE->TCP_CONNECTION_WAS_CLOS
  ([f3fa6cba](https://github.com/imrafaelmerino/vertx-effect/commit/f3fa6cba3b45c745f89a564b6a5f974859c9954c))
  - 💡 internal classes JsArrayExp JsObExp
  ([4e7a552c](https://github.com/imrafaelmerino/vertx-effect/commit/4e7a552c341e81d5fd84b3545cd54a13a4c5a548))
  - 💡 SeqVal -> ListExp
  ([2ac31c39](https://github.com/imrafaelmerino/vertx-effect/commit/2ac31c39cfbfaf9a53d58145a25629439d7c5160))
  - 💡 MapVal -> MapExp
  ([5d78e6d5](https://github.com/imrafaelmerino/vertx-effect/commit/5d78e6d5106181003caf5fccdeb0d91627a56d9e))
  - 💡 JsObjVal -> JsObjExp
  ([2f672dbb](https://github.com/imrafaelmerino/vertx-effect/commit/2f672dbb08f90d5b009246cbbacd2f4ed6eac8bf))
  - 💡 JsArrayVal -> JsArrayExp
  ([7fcfa91d](https://github.com/imrafaelmerino/vertx-effect/commit/7fcfa91da7c41c99e7989a5de5dc73544db20310))
  - 💡 organizing imports, override annotations
  ([b12c5337](https://github.com/imrafaelmerino/vertx-effect/commit/b12c53374777bdcb4c004cdf9746bc598b62b971))
  - 💡 returned type Any and All constructors
  ([11815aa6](https://github.com/imrafaelmerino/vertx-effect/commit/11815aa66c335fc97e1014f77fd582b1fe025ce2))
  - 💡 map function of Val can be generalized
  ([5a92f184](https://github.com/imrafaelmerino/vertx-effect/commit/5a92f18424139da65c694f2665574cc35342bb00))
  - 💡 test
  ([8c38c30c](https://github.com/imrafaelmerino/vertx-effect/commit/8c38c30cefeda4bd12813b124cc0e6ee10b0e708))

## Test
  - 💍 refactor tests
  ([ea153196](https://github.com/imrafaelmerino/vertx-effect/commit/ea1531962a6509f767867a056ed8631937b0be16))
  - 💍 refactor test
  ([f131455a](https://github.com/imrafaelmerino/vertx-effect/commit/f131455a647336e0dcb378a55382eeca16796047))

## Chore
  - 🤖 warnings
  ([037ca8a4](https://github.com/imrafaelmerino/vertx-effect/commit/037ca8a479dc7748f47aa55fbefa41042dac51a5))
  - 🤖 release 1.0.0 changes
  ([843e926c](https://github.com/imrafaelmerino/vertx-effect/commit/843e926c16271afe7dae70e4fb93398ebb4b9991))
  - 🤖 increase time JFR
  ([7e068ba8](https://github.com/imrafaelmerino/vertx-effect/commit/7e068ba8a8a062f4aeb688f53a675b96a19bcb65))
  - 🤖 removed comment
  ([4c1264c0](https://github.com/imrafaelmerino/vertx-effect/commit/4c1264c0c1a6d645a98999b8821c2e90775f656b))
  - 🤖 upgrading logback version
  ([7a6fcfaf](https://github.com/imrafaelmerino/vertx-effect/commit/7a6fcfaf3fa8938cc7ac7db88a2d8ff2072bcda5))
  - 🤖 logs and logback configuration
  ([599279ea](https://github.com/imrafaelmerino/vertx-effect/commit/599279eaa65d2e53ff60375ce2562f0809feba33))




