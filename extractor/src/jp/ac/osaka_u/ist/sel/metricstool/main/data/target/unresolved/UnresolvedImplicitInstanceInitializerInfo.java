package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.InstanceInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������ÖكX�^�e�B�b�N�C�j�V�����C�U��\���N���X
 * 
 * @author t-miyake
 *
 */
public class UnresolvedImplicitInstanceInitializerInfo extends UnresolvedInstanceInitializerInfo {

    /**
     * ���̃X�^�e�B�b�N�C�j�V�����C�U�����L����N���X�̖��������������ď�����
     * @param ownerClass ���̃X�^�e�B�b�N�C�j�V�����C�U�����L����N���X�̖��������
     */
    public UnresolvedImplicitInstanceInitializerInfo(UnresolvedClassInfo ownerClass) {
        super(ownerClass);
    }

    @Override
    public InstanceInitializerInfo resolve(TargetClassInfo usingClass,
            CallableUnitInfo usingMethod, ClassInfoManager classInfoManager,
            FieldInfoManager fieldInfoManager, MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == usingClass) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        final TargetClassInfo ownerClass = this.getOwnerClass().resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        
        this.resolvedInfo = ownerClass.getImplicitInstanceInitializer();

        return this.resolvedInfo;
    }
}
