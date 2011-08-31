package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �����������o(���\�b�h�C�R���X�g���N�^)�Ăяo����ۑ����邽�߂̃N���X
 * 
 * @author t-miyake, higo
 * @param <T> �����ς݂̌^
 */
public abstract class UnresolvedCallInfo<T extends CallInfo<?>> extends UnresolvedExpressionInfo<T> {

    /**
     * �I�u�W�F�N�g��������
     */
    public UnresolvedCallInfo() {

        MetricsToolSecurityManager.getInstance().checkAccess();

        this.typeArguments = new LinkedList<UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo>>();
        this.arguments = new LinkedList<UnresolvedExpressionInfo<?>>();

    }

    /**
     * �^�p�����[�^�g�p��ǉ�����
     * 
     * @param typeParameterUsage �ǉ�����^�p�����[�^�g�p
     */
    public final void addTypeArgument(final UnresolvedReferenceTypeInfo<?> typeParameterUsage) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeParameterUsage) {
            throw new NullPointerException();
        }

        this.typeArguments.add(typeParameterUsage);
    }

    /**
     * ������ǉ�
     * 
     * @param argument
     */
    public void addArgument(final UnresolvedExpressionInfo<? extends ExpressionInfo> argument) {
        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == argument) {
            throw new NullPointerException();
        }

        this.arguments.add(argument);
    }

    /**
     * ������ List ��Ԃ�
     * 
     * @return ������ List
     */
    public final List<UnresolvedExpressionInfo<?>> getArguments() {
        return Collections.unmodifiableList(this.arguments);
    }

    /**
     * �^�p�����[�^�g�p�� List ��Ԃ�
     * 
     * @return �^�p�����[�^�g�p�� List
     */
    public final List<UnresolvedReferenceTypeInfo<?>> getTypeArguments() {
        return Collections.unmodifiableList(this.typeArguments);
    }

    /**
     * 
     * @param unresolvedParameters
     * @return
     */
    protected final List<ExpressionInfo> resolveArguments(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new IllegalArgumentException();
        }

        //�@�����ςݎ��������i�[���邽�߂̕ϐ�
        final List<ExpressionInfo> parameters = new LinkedList<ExpressionInfo>();

        for (final UnresolvedExpressionInfo<?> unresolvedParameter : this.getArguments()) {

            ExpressionInfo parameter = unresolvedParameter.resolve(usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager);

            assert parameter != null : "resolveEntityUsage returned null!";

            if (parameter instanceof UnknownEntityUsageInfo) {

                // �N���X�Q�Ƃ������ꍇ
                if (unresolvedParameter instanceof UnresolvedClassReferenceInfo) {

                    final ExternalClassInfo externalClassInfo = UnresolvedClassReferenceInfo
                            .createExternalClassInfo((UnresolvedClassReferenceInfo) unresolvedParameter);
                    classInfoManager.add(externalClassInfo);
                    final ClassTypeInfo referenceType = new ClassTypeInfo(externalClassInfo);
                    for (final UnresolvedTypeInfo<?> unresolvedTypeArgument : ((UnresolvedClassReferenceInfo) unresolvedParameter)
                            .getTypeArguments()) {
                        final TypeInfo typeArgument = unresolvedTypeArgument.resolve(usingClass,
                                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                        referenceType.addTypeArgument(typeArgument);
                    }

                    // �g�p�ʒu���擾
                    final int fromLine = this.getFromLine();
                    final int fromColumn = this.getFromColumn();
                    final int toLine = this.getToLine();
                    final int toColumn = this.getToColumn();

                    /*// �v�f�g�p�̃I�[�i�[�v�f��Ԃ�
                    final UnresolvedExecutableElementInfo<?> unresolvedOwnerExecutableElement = this
                            .getOwnerExecutableElement();
                    final ExecutableElementInfo ownerExecutableElement = unresolvedOwnerExecutableElement
                            .resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                                    methodInfoManager);*/

                    parameter = new ClassReferenceInfo(referenceType, usingMethod, fromLine,
                            fromColumn, toLine, toColumn);
                    /*parameter.setOwnerExecutableElement(ownerExecutableElement);*/

                } else {
                    assert false : "Here shouldn't be reached!";
                }
            }
            parameters.add(parameter);
        }

        return parameters;
    }

    protected final List<ReferenceTypeInfo> resolveTypeArguments(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        //�@�����ς݌^�������i�[���邽�߂̕ϐ�
        final List<ReferenceTypeInfo> typeArguments = new LinkedList<ReferenceTypeInfo>();

        for (final UnresolvedReferenceTypeInfo<?> unresolvedTypeArgument : this.getTypeArguments()) {

            TypeInfo typeArgument = unresolvedTypeArgument.resolve(usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager);

            assert typeArgument != null : "resolveEntityUsage returned null!";

            typeArguments.add((ReferenceTypeInfo) typeArgument);
        }

        return typeArguments;
    }

    /**
     * �^�p�����[�^�g�p��ۑ����邽�߂̕ϐ�
     */
    protected List<UnresolvedReferenceTypeInfo<?>> typeArguments;

    /**
     * ������ۑ����邽�߂̕ϐ�
     */
    protected List<UnresolvedExpressionInfo<?>> arguments;

    /**
     * �G���[���b�Z�[�W�o�͗p�̃v�����^
     */
    protected static final MessagePrinter err = new DefaultMessagePrinter(new MessageSource() {
        public String getMessageSourceName() {
            return "UnresolvedCall";
        }
    }, MESSAGE_TYPE.ERROR);
}
