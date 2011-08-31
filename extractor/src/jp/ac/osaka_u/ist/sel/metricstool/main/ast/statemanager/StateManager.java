package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


/**
 * ��Ԃ��Ǘ�����C���^�t�F�[�X.
 * 
 * @author kou-tngt
 * 
 * @param <T> ��ԕω��̃g���K�ɂȂ�v�f�̌^
 */
public interface StateManager<T> {

    /**
     * ��ԕω���ʒm���郊�X�i��ǉ�����
     * @param listener
     */
    public void addStateChangeListener(StateChangeListener<T> listener);

    /**��ԕω���ʒm���郊�X�i���폜����
     * @param listener
     */
    public void removeStateChangeListener(StateChangeListener<T> listener);
}
