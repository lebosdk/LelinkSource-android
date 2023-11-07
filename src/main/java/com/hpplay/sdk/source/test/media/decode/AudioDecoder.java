package com.hpplay.sdk.source.test.media.decode;

import com.hpplay.sdk.source.test.media.extractor.AudioExtractor;
import com.hpplay.sdk.source.test.media.extractor.IExtractor;

public class AudioDecoder extends BaseDecoder {
    public AudioDecoder(String filePath) {
        super(filePath);
    }

    @Override
    protected IExtractor initExtractor(String filePath) {
        return new AudioExtractor(filePath);
    }
}
