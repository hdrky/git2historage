package jp.ac.osaka_u.ist.sel.metricstool.main.util;


import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;


/**
 * ���g����Q�ƂŎ����Ă���Set
 * 
 * @author kou-tngt
 *
 */
public class WeakHashSet<T> implements Set<T> {

    /**
     * ���������R���X�g���N�^�D
     * �����e�ʁC�������׌W���ŁC��̃Z�b�g���쐬����D
     */
    public WeakHashSet() {
        this.innerMap = new WeakHashMap<T, Object>();
    }

    /**
     * �����e�ʁC�������׌W���ł����Ƃ��쐬���D
     * �����̃R���N�V�����̗v�f�����̃Z�b�g�ɓ����
     * @param c ���̃Z�b�g�ɓ��ꂽ���v�f�������Ă���R���N�V����
     */
    public WeakHashSet(final Collection<? extends T> c) {
        this.innerMap = new WeakHashMap<T, Object>();
        for (final T key : c) {
            this.innerMap.put(key, DUMMY);
        }
    }

    /**
     * �����e��initialCapacity�ŋ�̃Z�b�g���쐬����D
     * @param initialCapacity ���̃Z�b�g�̏����e��
     */
    public WeakHashSet(final int initialCapacity) {
        this.innerMap = new WeakHashMap<T, Object>(initialCapacity);
    }

    /**
     * �����e��initialCapacity�ŕ��׌W��loadFactor��p����A��̃Z�b�g���쐬����D
     * @param initialCapacity �����e��
     * @param loadFactor �̃Z�b�g�̕��׌W��
     */
    public WeakHashSet(final int initialCapacity, final float loadFactor) {
        this.innerMap = new WeakHashMap<T, Object>(initialCapacity, loadFactor);
    }

    /* (non-Javadoc)
     * @see java.util.AbstractCollection#iterator()
     */
    public Iterator<T> iterator() {
        return this.innerMap.keySet().iterator();
    }

    /* (non-Javadoc)
     * @see java.util.AbstractCollection#size()
     */
    public int size() {
        return this.innerMap.size();
    }

    /* (non-Javadoc)
     * @see java.util.Set#add(java.lang.Object)
     */
    public boolean add(final T o) {
        return this.innerMap.put(o, DUMMY) == null;
    }

    /* (non-Javadoc)
     * @see java.util.Set#addAll(java.util.Collection)
     */
    public boolean addAll(final Collection<? extends T> c) {
        boolean result = false;
        for (final T key : c) {
            if (this.add(key)) {
                result = true;
            }
        }
        return result;
    }

    /* (non-Javadoc)
     * @see java.util.Set#clear()
     */
    public void clear() {
        this.innerMap.clear();
    }

    /* (non-Javadoc)
     * @see java.util.Set#contains(java.lang.Object)
     */
    public boolean contains(final Object o) {
        return this.innerMap.containsKey(o);
    }

    /* (non-Javadoc)
     * @see java.util.Set#containsAll(java.util.Collection)
     */
    public boolean containsAll(final Collection<?> c) {
        return this.innerMap.keySet().containsAll(c);
    }

    /* (non-Javadoc)
     * @see java.util.Set#isEmpty()
     */
    public boolean isEmpty() {
        return this.innerMap.isEmpty();
    }

    /* (non-Javadoc)
     * @see java.util.Set#remove(java.lang.Object)
     */
    public boolean remove(final Object o) {
        return this.innerMap.remove(o) != null;
    }

    /* (non-Javadoc)
     * @see java.util.Set#removeAll(java.util.Collection)
     */
    public boolean removeAll(final Collection<?> c) {
        boolean result = false;
        for (final Object key : c) {
            if (this.remove(key)) {
                result = true;
            }
        }
        return result;
    }

    /* (non-Javadoc)
     * @see java.util.Set#retainAll(java.util.Collection)
     */
    public boolean retainAll(final Collection<?> c) {
        if (this.size() == c.size() && this.containsAll(c)) {
            return false;
        }

        return this.innerMap.keySet().retainAll(c);
    }

    /* (non-Javadoc)
     * @see java.util.Set#toArray()
     */
    public Object[] toArray() {
        return this.innerMap.keySet().toArray();
    }

    /* (non-Javadoc)
     * @see java.util.Set#toArray(K[])
     */
    public <K> K[] toArray(final K[] a) {
        return this.innerMap.keySet().toArray(a);
    }

    /**
     * �_�~�[�̒l�p�I�u�W�F�N�g
     */
    private static final Object DUMMY = "Dummy";

    /**
     * �����ŗ��p���� {@link WeakHashMap}
     */
    private final WeakHashMap<T, Object> innerMap;
}
