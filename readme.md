Amcrest Radio
===

Background streaming of audio channels from Amcrest cameras.

### camera api

Streams G.711 a-law formatted audio packets.  See section 4.3.4 of the api manual.

```
 curl --digest \
      -u 'user:password' \
      'http://192.168.1.123/cgi-bin/audio.cgi?action=getAudio&httptype=singlepart&channel=1' \
      > /dev/null
```

- httptype: 'singlepart' || 'multipart'
- channel: integer (the audio channel)
- uses digest authentication

### references
- [Amcrest API doc](https://s3.amazonaws.com/amcrest-files/Amcrest+HTTP+API+3.2017.pdf)
- [PCMA Audio Format](https://developer.android.com/reference/android/net/rtp/AudioCodec.html#PCMA)