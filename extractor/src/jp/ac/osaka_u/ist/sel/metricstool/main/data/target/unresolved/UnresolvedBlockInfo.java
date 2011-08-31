package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;


/**
 * if����while���Ȃǂ̃��\�b�h���̍\���i�u���b�N�j��\�����߂̃N���X
 * 
 * @author higo
 * @param <T> �����ς݂̃u���b�N�̌^
 * 
 */
public abstract class UnresolvedBlockInfo<T extends BlockInfo> extends UnresolvedLocalSpaceInfo<T>
        implements UnresolvedStatementInfo<T> {

    /**
     * ���̃u���b�N�̊O���Ɉʒu����u���b�N��^���āC�I�u�W�F�N�g��������
     * 
     * @param outerSpace ���̃u���b�N�̊O���Ɉʒu����u���b�N
     * 
     */
    public UnresolvedBlockInfo(final UnresolvedLocalSpaceInfo<?> outerSpace) {
        super(outerSpace);
    }

    public void initBody() {
        this.statements.clear();
    }

    /**
     * ���̃u���b�N���������Ԃ�Ԃ�
     * @return ���̃u���b�N����������
     */
    public UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> getOuterSpace() {
        return (UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo>) this.getOuterUnit();
    }
}
