package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.io.Serializable;
import java.util.Set;


/**
 * ���s�\�ȒP�ʂ�\���v�f
 * 
 * @author higo
 *
 */
public interface ExecutableElementInfo extends Position, Serializable {

    /**
     * �ϐ��̎g�p��Set��Ԃ�
     * 
     * @return �ϐ��̎g�p��Set
     */
    Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages();

    /**
     * ��`����Ă���ϐ���Set��Ԃ�
     * 
     * @return ���̒��Œ�`����Ă���ϐ���Set
     */
    Set<VariableInfo<? extends UnitInfo>> getDefinedVariables();

    /**
     * ���\�b�h�Ăяo����Ԃ�
     * 
     * @return ���\�b�h�Ăяo��
     */
    Set<CallInfo<? extends CallableUnitInfo>> getCalls();

    /**
     * �I�[�i�[���\�b�h��Ԃ�
     * 
     * @return �I�[�i�[���\�b�h
     */
    CallableUnitInfo getOwnerMethod();

    /**
     * ���ڏ��L�����Ԃ�Ԃ�
     * 
     * @return ���ڏ��L������
     */
    LocalSpaceInfo getOwnerSpace();

    /**
     * �e�L�X�g�\��(String�^)��Ԃ�
     * 
     * @return�@�e�L�X�g�\��(String�^)��Ԃ�
     */
    String getText();

    /**
     * ��������\���������O��Set��Ԃ�
     * 
     * @return�@��������\���������O��Set
     */
    Set<ReferenceTypeInfo> getThrownExceptions();

    /**
     * ���̃v���O�����v�f�̃f�B�[�v�R�s�[��Ԃ�.
     * �������C�����������ʂɈʒu����I�u�W�F�N�g�̂݃f�B�[�v�R�s�[�D
     * �����̏�ʂɈʒu������̂ɂ��Ă̓V�����[�R�s�[.
     * 
     * @return
     */
    ExecutableElementInfo copy();
}