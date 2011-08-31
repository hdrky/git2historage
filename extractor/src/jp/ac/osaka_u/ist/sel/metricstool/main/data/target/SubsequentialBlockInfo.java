package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * ElseBlock, CatchBlock�CFinallyBlock�̂悤�ɁC
 * ����Block�̑��݉��ł̂ݒ�`�����u���b�N�ł��邱�Ƃ������C���^�t�F�C�X
 * 
 * @author g-yamada
 *
 * @param <T> ���̃u���b�N���������Ă���u���b�N�̌^ 
 */
public interface SubsequentialBlockInfo<T extends BlockInfo> {

    /**
     * ���̃u���b�N���������Ă���u���b�N��Ԃ�
     *
     * @return ���̃u���b�N���������Ă���u���b�N
     */
    public T getOwnerBlock();

    /**
     * ���̃u���b�N���������Ă���u���b�N���Z�b�g����
     * 
     * @param ownerBlock ���̃u���b�N���������Ă���u���b�N
     */
    public void setOwnerBlock(T ownerBlock);
}
