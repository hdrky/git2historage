package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import java.util.EventListener;


/**
 * ��ԕω��̒ʒm���󂯎�郊�X�i
 * 
 * @author kou-tngt
 *
 * @param <T>�@��ԕω��̃g���K�ƂȂ�v�f�̌^
 */
public interface StateChangeListener<T> extends EventListener {

    /**
     * ��ԕω���ʒm���郁�\�b�h
     * @param event ��ԕω���\���C�x���g
     */
    public void stateChanged(StateChangeEvent<T> event);
}
