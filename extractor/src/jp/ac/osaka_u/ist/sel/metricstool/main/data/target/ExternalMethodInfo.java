package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * �O���N���X�ɒ�`����Ă��郁�\�b�h����ۑ����邽�߂̃N���X
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public final class ExternalMethodInfo extends MethodInfo {

    /**
     * �O���N���X�ɒ�`����Ă��郁�\�b�h�I�u�W�F�N�g������������
     * �A�N�Z�X����q�܂ŕ������Ă���ꍇ
     *  
     * @param methodName ���\�b�h��
     */
    public ExternalMethodInfo(final Set<ModifierInfo> modifiers, final String methodName,
            final boolean instance) {

        super(modifiers, methodName, instance, getDummyPosition(), getDummyPosition(),
                getDummyPosition(), getDummyPosition());

        this.setReturnType(UnknownTypeInfo.getInstance());
    }

    /**
     * �O���N���X�ɒ�`����Ă��郁�\�b�h�I�u�W�F�N�g������������
     * 
     * @param methodName ���\�b�h��
     */
    public ExternalMethodInfo(final String methodName) {

        super(new HashSet<ModifierInfo>(), methodName, true, getDummyPosition(),
                getDummyPosition(), getDummyPosition(), getDummyPosition());

        this.setOuterUnit(ExternalClassInfo.UNKNOWN);
        this.setReturnType(UnknownTypeInfo.getInstance());
    }

    /**
     * ���SortedSet��Ԃ�
     */
    @Override
    public SortedSet<StatementInfo> getStatements() {
        return Collections.unmodifiableSortedSet(new TreeSet<StatementInfo>());
    }
}
