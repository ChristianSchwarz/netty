/*
 * Copyright 2017 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.channel;

abstract class PendingBytesTracker implements MessageSizeEstimator.Handle {
    abstract void incrementPendingOutboundBytes(long bytes);
    abstract void decrementPendingOutboundBytes(long bytes);

    private PendingBytesTracker() {
        // Should only be created via the static method.
    }

    static PendingBytesTracker newTracker(Channel channel) {
        if (channel.pipeline() instanceof DefaultChannelPipeline) {
            final DefaultChannelPipeline pipeline = (DefaultChannelPipeline) channel.pipeline();
            return new PendingBytesTracker() {
                @Override
                public void incrementPendingOutboundBytes(long bytes) {
                    pipeline.incrementPendingOutboundBytes(bytes);
                }

                @Override
                public void decrementPendingOutboundBytes(long bytes) {
                    pipeline.decrementPendingOutboundBytes(bytes);
                }

                @Override
                public int size(Object msg) {
                    return pipeline.estimatorHandle().size(msg);
                }
            };
        } else {
            final MessageSizeEstimator.Handle estimator = channel.config().getMessageSizeEstimator().newHandle();
            final ChannelOutboundBuffer buffer = channel.unsafe().outboundBuffer();
            // We need to guard against null as channel.unsafe().outboundBuffer() may returned null
            // if the channel was already closed when constructing the PendingBytesTracker.
            // See https://github.com/netty/netty/issues/3967
            if (buffer == null) {
                return new PendingBytesTracker() {
                    @Override
                    public void incrementPendingOutboundBytes(long bytes) {
                        // noop
                    }

                    @Override
                    public void decrementPendingOutboundBytes(long bytes) {
                        // noop
                    }

                    @Override
                    public int size(Object msg) {
                        return estimator.size(msg);
                    }
                };
            }
            return new PendingBytesTracker() {
                @Override
                public void incrementPendingOutboundBytes(long bytes) {
                    buffer.incrementPendingOutboundBytes(bytes);
                }

                @Override
                public void decrementPendingOutboundBytes(long bytes) {
                    buffer.decrementPendingOutboundBytes(bytes);
                }

                @Override
                public int size(Object msg) {
                    return estimator.size(msg);
                }
            };
        }
    }
}
