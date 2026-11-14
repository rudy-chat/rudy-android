package org.rudy.chat.messenger;

public interface GenericProvider<F, T> {
    T provide(F obj);
}
