package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.TypeArgumentStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.TypeDescriptionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.TypeParameterStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.BuiltinTypeToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArbitraryTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassImportStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExtendsTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedSuperTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeParameterTypeInfo;


/**
 * �����ȊO�ł̌^�Q�Ƃ̏����\�z����r���_�[�D
 * 
 * @author kou-tngt, t-miyake
 *
 */
public class TypeBuilder extends CompoundDataBuilder<UnresolvedTypeInfo<? extends TypeInfo>> {

    /**
     * ������BuildDataManager��p���ď��������s���D
     * 
     * @param buildDataManager�@���̃r���_�[�ŗ��p����f�[�^�Ǘ���
     */
    public TypeBuilder(final BuildDataManager buildDataManager) {
        if (null == buildDataManager) {
            throw new NullPointerException("nameSpaceManager is null.");
        }

        this.buildDataManager = buildDataManager;

        this.addStateManager(this.typeStateManager);
        this.addStateManager(new TypeArgumentStateManager());
        this.addStateManager(typePrameterStateManager);
        this.addInnerBuilder(this.identifierBuilder);
    }

    /**
     * �r�W�^�[���m�[�h�ɓ��������̃C�x���g�ʒm���󂯎��D
     * @param �r�W�b�g�C�x���g
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.CompoundDataBuilder#entered(jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent)
     */
    @Override
    public void entered(final AstVisitEvent event) {
        super.entered(event);

        if (this.isActive() && this.typeStateManager.isEntered()) {
            //�^��`����
            final AstToken token = event.getToken();

            if (null == this.primitiveType && null == this.voidType
                    && token instanceof BuiltinTypeToken) {
                //�g�[�N������g�ݍ��݌^����������������
                final BuiltinTypeToken typeToken = (BuiltinTypeToken) token;
                if (typeToken.isPrimitiveType()) {
                    this.primitiveType = typeToken.getType();
                } else if (typeToken.isVoidType()) {
                    this.voidType = typeToken.getType();
                }
            }
        }
    }

    /**
     * �r�W�^�[���m�[�h����o�����̃C�x���g�ʒm���󂯎��D
     * @param �r�W�b�g�C�x���g
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.CompoundDataBuilder#exited(jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent)
     */
    @Override
    public void exited(final AstVisitEvent event) throws ASTParseException {
        super.exited(event);

        if (this.isActive() && this.typeStateManager.isEntered()) {
            //�^��`����
            final AstToken token = event.getToken();
            if (token.isArrayDeclarator()) {
                //�z��L�q������΂�����J�E���g���Ď������ɂ���
                this.arrayCount++;
            } else if (token.isNameSeparator()) {
                //���O��؂�ɓ��B�������ɉ\�Ȃ�ߋ��ɍ\�z�����^��A������
                // TODO this.combineBuiltTypes();
            }
        }
    }

    @Override
    public void stateChanged(final StateChangeEvent<AstVisitEvent> event) {
        if (this.isActive()) {
            final StateChangeEventType type = event.getType();
            if (type.equals(TypeDescriptionStateManager.TYPE_STATE.ENTER_TYPE)) {
                //�^�錾�̒��ɓ������̂Ŏ��ʎq�r���_�[���N��
                this.identifierBuilder.activate();
            } else if (type.equals(TypeDescriptionStateManager.TYPE_STATE.EXIT_TYPE)) {
                //�^�錾�̒�����o���̂ŁC
                //���̌��ʂ��\�z���ēo�^
                this.buildType();
                if (!this.typeStateManager.isEntered()) {
                    //�S�Ă̌^�\�z���̊O�ɏo���Ȃ环�ʎq�r���_�[���A�N�e�B�u��
                    this.identifierBuilder.deactivate();
                }
            } else if (type
                    .equals(TypeArgumentStateManager.TYPE_ARGUMENT_STATE.ENTER_TYPE_ARGUMENTS)) {
                //�^�����Q�̒�`���ɓ������̂ŁC�V���Ȍ^�����Q�̏������郊�X�g���X�^�b�N�ɂ�
                this.typeArgumentsLists
                        .push(new ArrayList<UnresolvedTypeInfo<? extends TypeInfo>>());
            } else if (type
                    .equals(TypeArgumentStateManager.TYPE_ARGUMENT_STATE.EXIT_TYPE_ARGUMENTS)) {
                //�^�����Q�̒�`�����I������̂ŁC�X�^�b�N�̈�ԏ�̃��X�g���C�����ݗ��p�ł���^�p�����[�^�Q�Ƃ��Ď��o���D
                if (!this.typeArgumentsLists.isEmpty()) {
                    this.availableTypeArugments = this.typeArgumentsLists.pop();
                    //this.buildType();
                } else {
                    assert (false) : "Illegal state: requested type arguments were not available.";
                }
            } else if (type.equals(TypeArgumentStateManager.TYPE_ARGUMENT_STATE.EXIT_TYPE_ARGUMENT)) {
                //�^�����L�q������o���̂ŁC���߂̌^�������o���Č^�������Ƃ��ēo�^����
                if (!this.typeArgumentsLists.isEmpty()) {
                    this.typeArgumentsLists.peek().add(this.popLastBuiltData());
                } else {
                    assert (false) : "Illegal state: type argument could not be registered.";
                }
            } else if (type
                    .equals(TypeArgumentStateManager.TYPE_ARGUMENT_STATE.ENTER_TYPE_WILDCARD)) {
                //���C���h�J�[�h�L�q���ɓ������̂ŃC���N�������g
                this.inWildCardCount++;
            } else if (type.equals(TypeArgumentStateManager.TYPE_ARGUMENT_STATE.EXIT_TYPE_WILDCARD)) {
                //���C���h�J�[�h�L�q������o��̂ŁC�^���(����)�����擾���Č^����o�^
                final UnresolvedTypeInfo<? extends TypeInfo> bounds = this.getCurrentBounds();

                assert (bounds != null) : "Illegal state: type upper bounds was not specified.";

                if (bounds instanceof UnresolvedExtendsTypeInfo
                        || bounds instanceof UnresolvedSuperTypeInfo) {
                    this.registBuiltData(bounds);
                } else {
                    //���E��UnresolvedExtendsTypeInfo�ł�UnresolvedExtendsTypeInfo�ł��Ȃ���΃��C���h�J�[�h�P��
                    this.registBuiltData(UnresolvedArbitraryTypeInfo.getInstance());
                }
                this.currentBounds = null;
                this.inWildCardCount--;
            } else if (this.inWildCardCount > 0
                    && type.equals(TypeParameterStateManager.TYPE_PARAMETER.EXIT_TYPE_UPPER_BOUNDS)) {
                //���C���h�J�[�h���Ō^�����񂪂������̂ŁC�����o�^
                this.currentBounds = this.popLastBuiltData();
                //                UnresolvedExtendsTypeInfo e = new UnresolvedExtendsTypeInfo(this.currentUpperBounds);
                if (this.currentBounds instanceof UnresolvedReferenceTypeInfo<?>) {
                    this.currentBounds = new UnresolvedExtendsTypeInfo(
                            (UnresolvedReferenceTypeInfo<?>) this.currentBounds);
                }
            } else if (this.inWildCardCount > 0
                    && type.equals(TypeParameterStateManager.TYPE_PARAMETER.EXIT_TYPE_LOWER_BOUNDS)) {
                //���C���h�J�[�h���Ō^������񂪂������̂ŁC�����o�^
                this.currentBounds = this.popLastBuiltData();
                if (this.currentBounds instanceof UnresolvedReferenceTypeInfo<?>) {
                    this.currentBounds = new UnresolvedSuperTypeInfo(
                            (UnresolvedReferenceTypeInfo<?>) this.currentBounds);
                }
            }
        }
    }

    /**
     * �^���(���͉���)�����擾����
     * @return�@�^���(���͉���)�̏��
     */
    protected UnresolvedTypeInfo<? extends TypeInfo> getCurrentBounds() {
        return this.currentBounds;
    }

    /**
     * �^�����\�z���郁�\�b�h�D
     * �^��`�m�[�h����o�����C�^���ȊO��\���g�[�N���Ɉڂ��Ă��܂������C�^�p�����[�^�Q�̍\�z���I�������Ƃ��ɌĂяo�����D
     */
    protected void buildType() {
        UnresolvedTypeInfo<? extends TypeInfo> resultType = null;

        if (null != this.primitiveType) {
            //�g�ݍ��݌^�̃f�[�^������Ă����̂ł�����g��
            resultType = this.primitiveType;
            this.primitiveType = null;
        } else if (null != this.voidType) {
            resultType = this.voidType;
            this.voidType = null;
        } else if (this.identifierBuilder.hasBuiltData()) {
            //���ʎq��񂪍\�z����Ă����̂ŁC������g���ĎQ�ƌ^�����D
            final String[] identifier = this.identifierBuilder.popLastBuiltData();

            assert (0 != identifier.length) : "Illegal state: identifier was not built.";

            UnresolvedTypeParameterTypeInfo typeParameterType = null;

            //���̖��O�Ō^�p�����[�^��T���Ă݂�
            if (identifier.length == 1) {
                typeParameterType = this.buildDataManager.getTypeParameterType(identifier[0]);
            }

            if (null != typeParameterType) {
                //���������̂Ō^�p�����[�^
                resultType = typeParameterType;

                //TODO �^�p�����[�^�Ɍ^�������t�����ꂪ�������炻���o�^����d�g�݂����K�v�����邩��

            } else {
                //������Ȃ������̂ŎQ�ƌ^
                //�Q�ƌ^���쐬
                final UnresolvedClassTypeInfo referenceType = new UnresolvedClassTypeInfo(
                        UnresolvedClassImportStatementInfo
                                .getClassImportStatements(this.buildDataManager
                                        .getAllAvaliableNames()), identifier);

                //�g����^����������Γo�^���Ă��܂��D
                if (null != this.availableTypeArugments) {
                    for (final UnresolvedTypeInfo<? extends TypeInfo> type : this.availableTypeArugments) {

                        // C#�Ȃǂ͎Q�ƌ^�ȊO���^�����Ɏw��\�Ȃ̂őΏ�����Ђ悤�����邩��
                        if (type instanceof UnresolvedTypeInfo<?>) {
                            referenceType.addTypeArgument((UnresolvedTypeInfo<?>) type);
                        }
                    }

                    this.availableTypeArugments = null;
                }
                resultType = referenceType;
            }

        } else if (this.hasBuiltData()) {
            //���ʎq���V��������đ���͂��Ȃ����ǉߋ��ɍ\�z�����f�[�^���������̂ŁC������g���Ă���
            resultType = this.popLastBuiltData();

        } else {
            assert (false) : "Illegal state: type can not built.";
        }

        if (0 < this.arrayCount) {
            //�z��L�q���������̂ł����̌^��z��ɂ��Ă���
            resultType = UnresolvedArrayTypeInfo.getType(resultType, this.arrayCount);
        }

        this.arrayCount = 0;
        this.registBuiltData(resultType);
    }

    /**
     * �r�W�^�[�����O��؂�m�[�h����o�鎞�ɌĂяo�����D
     * �\�ł���΁C�ߋ��ɍ\�z����2�̌^����p���āC1�̒����Q�ƌ^�����\�z����D
     * �\�z�ł��Ȃ��̂ł���Γ��ɉ������Ȃ��D
     */
    protected void combineBuiltTypes() {
        if (this.hasBuiltData() && this.getBuiltDataCount() == 2) {
            assert false : "Illegal state:";
            /*final UnresolvedTypeInfo second = this.popLastBuiltData();
            final UnresolvedTypeInfo first = this.popLastBuiltData();

            //�ǂ�����Q�ƌ^�ł���͂�
            assert (first instanceof UnresolvedReferenceTypeInfo) : "Illegal state: firstType was not unresolvedReference.";
            assert (second instanceof UnresolvedReferenceTypeInfo) : "Illegal state: firstType was not unresolvedReference.";

            if (first instanceof UnresolvedReferenceTypeInfo
                    && second instanceof UnresolvedReferenceTypeInfo) {
                final UnresolvedReferenceTypeInfo secondReference = (UnresolvedReferenceTypeInfo) second;
                final String[] names = secondReference.getFullReferenceName();
                //owner�����V�����Q�ƌ^�����
                final UnresolvedReferenceTypeInfo result = new UnresolvedReferenceTypeInfo(
                        this.buildDataManager.getAvailableNameSpaceSet(), names,
                        (UnresolvedReferenceTypeInfo) first);

                //�^���������Z�b�g����
                for (final UnresolvedReferenceTypeInfo usage : secondReference.getTypeArguments()) {
                    result.addTypeArgument(usage);
                }

                //���ʂ��\�z�ς݂̌^�Ƃ��ēo�^
                this.registBuiltData(result);
            }*/
        }
    }

    @Override
    public void clearBuiltData() {
        super.clearBuiltData();

        this.typeArgumentsLists.clear();
        this.availableTypeArugments = null;
    }

    /**
     * �z��L�q�q�̐����J�E���g����
     */
    private int arrayCount;

    /**
     * �^�Q�ƂŃ��C���h�J�[�h���g��ꂽ���̏�������L�����Ă���
     */
    private UnresolvedTypeInfo<? extends TypeInfo> currentBounds;

    /**
     * �^�����Q���L�^���Ă����X�^�b�N
     */
    private final Stack<List<UnresolvedTypeInfo<? extends TypeInfo>>> typeArgumentsLists = new Stack<List<UnresolvedTypeInfo<? extends TypeInfo>>>();

    /**
     * �\�z���I����ė��p�ł���^�����Q
     */
    private List<UnresolvedTypeInfo<? extends TypeInfo>> availableTypeArugments;

    /**
     * ���C���h�J�[�h���̍\�z�ɓ������[�����J�E���g
     */
    private int inWildCardCount;

    /**
     * �\�z������{�^���L�^����
     */
    private UnresolvedTypeInfo<? extends TypeInfo> primitiveType;

    /**
     * �\�z����void�^���L�^����
     */
    private UnresolvedTypeInfo<? extends TypeInfo> voidType;

    /**
     * ���ʎq�����\�z����r���_�[
     */
    private final IdentifierBuilder identifierBuilder = new IdentifierBuilder();

    /**
     * �\�z���Ǘ���
     */
    private final BuildDataManager buildDataManager;

    /**
     * �^������`���Ɋւ����ԊǗ�������X�e�[�g�}�l�[�W��
     */
    private final TypeDescriptionStateManager typeStateManager = new TypeDescriptionStateManager();

    private final TypeParameterStateManager typePrameterStateManager = new TypeParameterStateManager();
}
