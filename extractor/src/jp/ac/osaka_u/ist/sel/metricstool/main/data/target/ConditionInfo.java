package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * if����for���̏�����\���N���X
 * 
 * @author higo
 *
 */
public interface ConditionInfo extends ExecutableElementInfo {

    ConditionalBlockInfo getOwnerConditionalBlock();

    void setOwnerConditionalBlock(ConditionalBlockInfo ownerConditionalBlock);
}
