package jp.ac.osaka_u.ist.sel.metricstool.main.util;


import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 * �C�ӂ̃^�C�~���O�ŕ��邱�Ƃ��o����u���b�L���O�L���[
 * @author kou-tngt
 *
 * @param <E>�@�L���[�̗v�f�̌^
 */
public class ClosableLinkedBlockingQueue<E> extends LinkedBlockingQueue<E> {
    /**
     * 
     */
    private static final long serialVersionUID = -4159234755316262135L;

    /**
     * �L���[�ɓ���邱�Ƃ��ł���v�f����Ԃ�.
     * {@link #close()}���Ă΂ꂽ��͏��0��Ԃ�.
     * @return �L���[�ɓ���邱�Ƃ��ł���v�f��.
     * @see java.util.concurrent.LinkedBlockingQueue#remainingCapacity()
     */
    @Override
    public int remainingCapacity() {
        if (this.closed) {
            return 0;
        } else {
            return super.remainingCapacity();
        }
    }

    /**
     * �L���[�ɗv�f��ǉ�����
     * {@link #close()}���Ă΂ꂽ��͏�Ɏ��s����.
     * @param element �L���[�ɓ����v�f
     * @return �L���[�ɗv�f���ǉ��ł����ture, ���s�����false
     * @see java.util.AbstractQueue#add(java.lang.Object)
     */
    @Override
    public boolean add(final E element) {
        if (this.closed) {
            return false;
        } else {
            return super.add(element);
        }
    }

    /**
     * �L���[�ɗv�f��ǉ�����
     * {@link #close()}���Ă΂ꂽ��͏�Ɏ��s����.
     * @param element �L���[�ɓ����v�f
     * @return �L���[�ɗv�f���ǉ��ł����ture, ���s�����false
     * @see java.util.concurrent.LinkedBlockingQueue#offer(java.lang.Object)
     */
    @Override
    public boolean offer(final E element) {
        if (this.closed) {
            return false;
        } else {
            return super.offer(element);
        }
    }

    /**
     * �L���[�ɗv�f���ǉ��ł���܂ŁC��莞�ԑ҂��\�b�h.
     * {@link #close()}���Ă΂ꂽ��͏�ɑ����Ɏ��s����.
     * @param element �L���[�ɓ����v�f
     * @param timeout �^�C���A�E�g���鎞��
     * @param unit �^�C���A�E�g���鎞�Ԃ̒P��
     * @return �L���[�ɗv�f���ǉ��ł����ture, ���s�����false
     * @see java.util.concurrent.LinkedBlockingQueue#offer(java.lang.Object, long, java.util.concurrent.TimeUnit)
     */
    @Override
    public boolean offer(final E element, final long timeout, final TimeUnit unit)
            throws InterruptedException {
        if (this.closed) {
            return false;
        } else {
            return super.offer(element, timeout, unit);
        }
    }

    /**
     * ���̃L���[�����
     */
    public void close() {
        this.closed = true;
    }

    /**
     * �L���[�������Ă��邩�ǂ�����Ԃ�.
     * @return �����Ă����ture, �����łȂ����false
     */
    public boolean isClosed() {
        return this.closed;
    }

    /**
     * ����ꂽ���Ƃ�\���t���O
     */
    private boolean closed = false;
}
