package ru.nsu.ccfit.boltava.warehouse;

import ru.nsu.ccfit.boltava.threadpool.BlockingQueue;

public class Storage<ItemType> {

    private BlockingQueue<ItemType> mStorage;

    public Storage(long size) {
        mStorage = new BlockingQueue<ItemType>(size);
    }

    public void put(ItemType item) throws InterruptedException {
        mStorage.enqueue(item);
    }

    public ItemType get() throws InterruptedException {
        return mStorage.dequeue();
    }

    public long getItemsCount() {
        return mStorage.getSize();
    }

}
