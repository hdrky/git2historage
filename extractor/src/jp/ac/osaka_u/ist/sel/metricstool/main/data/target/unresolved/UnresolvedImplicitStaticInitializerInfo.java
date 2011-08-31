package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StaticInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������ÖكC���X�^���X�C�j�V�����C�U��\���N���X
 * 
 * @author t-miyake
 *
 */
public class UnresolvedImplicitStaticInitializerInfo extends UnresolvedStaticInitializerInfo {

    /**
     * ���̃C���X�^���X�C�j�V�����C�U�����L����N���X�̖���������^���ď�����
     * @param ownerClass ���̃C���X�^���X�C�j�V�����C�U�����L����N���X�̖��������
     */
    public UnresolvedImplicitStaticInitializerInfo(UnresolvedClassInfo ownerClass) {
        super(ownerClass);
    }

    @Override
    public StaticInitializerInfo resolve(TargetClassInfo usingClass, CallableUnitInfo usingMethod,
            ClassInfoManager classInfoManager, FieldInfoManager fieldInfoManager,
            MethodInfoManager methodInfoManager) {
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

        this.resolvedInfo = ownerClass.getImplicitStaticInitializer();
        
        return this.resolvedInfo;
    }

}
