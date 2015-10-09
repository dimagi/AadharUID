# AadharUID

AadharUID is an all-purpose scanner app<sup>1</sup> for the national Indian Aadhar<sup>2</sup> health card.


1. AadharUID does no scanning itself, but can be used with any popular Android barcode scanner,
such as QR Droid or Barcode.
2. also spelled Aadhaar


## Callout interface

AadharUID does not do much as an app by itself, but rather provides an interface through which
other applications can call it. It was built for integration with CommCare, but any Android app
can access it through the following callout interface.

### Making a request

```java
Intent intent = new Intent("org.commcarehq.aadharuid.barcode.LAUNCH");
// optional: specify which field you want returned to you
// as `intent.getExtra("odk_intent_data")`.
// default value is "statusText"
intent.putExtra("odk_intent_data_field", "uid");
```

### Receiving a response

#### odk_intent_bundle
```java
intent.getBundleExtra("odk_intent_bundle")
```

This bundle will contain the following fields:
- Special values:
  - `statusCode` either `"0"` (success) or `"1"` (parse error)
  - `statusText` either `"✓"` (success) or `"✗"` (any failure)
  - `rawString` the raw string represented by the QR Code or barcode
- Values straight from the Aadhar barcode
  - `co`
  - `dist`
  - `dob` normalized to YYYY-MM-DD
  - `dobGuess` dob if provided or else yob-06-01
  - `gender` normalized to `M` or `F`
  - `house`
  - `lm`
  - `loc`
  - `name`
  - `pc`
  - `po`
  - `state`
  - `street`
  - `subdist`
  - `uid`
  - `vtc`
  - `yob`

Some Aadhar cards use a 2D barcode. On these cards,
only the `uid` is encoded; the rest of the values will be `""`.

#### odk_intent_data
```java
intent.getStringExtra("odk_intent_data")
```

This value will be set to whatever field was requested through
the `odk_intent_data_field` extra. For example, if `odk_intent_data_field` was set to `"uid"` in the request intent,
then the `odk_intent_data` extra will be set to the `uid` value.
By default it is set to `statusText`.
