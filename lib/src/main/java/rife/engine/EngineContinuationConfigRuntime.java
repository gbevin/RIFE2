/*
 * Copyright 2001-2022 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.engine;

import rife.continuations.*;

public class EngineContinuationConfigRuntime extends ContinuationConfigRuntime {
    private final Gate gate_;

    EngineContinuationConfigRuntime(Gate gate) {
        gate_ = gate;
    }

    public ContinuationManager getContinuationManager(Object executingContinuable) {
        return gate_.continuationManager_;
    }

    public boolean cloneContinuations(Object executingContinuable) {
        return false;
    }

    // TODO : hook up continuation runtime settings
//    public long getContinuationDuration()
//    {
//        return RifeConfig.Engine.getContinuationDuration();
//    }
//
//    public int getContinuationPurgeFrequency()
//    {
//        return RifeConfig.Engine.getContinuationPurgeFrequency();
//    }
//
//    public int getContinuationPurgeScale()
//    {
//        return RifeConfig.Engine.getContinuationPurgeScale();
//    }
}
