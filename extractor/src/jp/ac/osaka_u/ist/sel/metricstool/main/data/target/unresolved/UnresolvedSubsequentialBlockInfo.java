package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;

/**
 * ElseBlock, CatchBlock, FinallyBLock �̂悤���ɁC
 * ����Block�̑��݉��ł̂ݒ�`����関�����u���b�N�ł��邱�Ƃ�\���C���^�t�F�C�X
 * @author g-yamada
 *
 * @param <T> ���̃u���b�N���������Ă��関�����u���b�N�̌^
 */
public interface UnresolvedSubsequentialBlockInfo<T extends UnresolvedBlockInfo<? extends BlockInfo>>{
/**
 * ���̃u���b�N���������Ă��関�����u���b�N��Ԃ�
 * 
 * @return ���̃u���b�N���������Ă��関�����u���b�N
 */
    public T getOwnerBlock();

}
