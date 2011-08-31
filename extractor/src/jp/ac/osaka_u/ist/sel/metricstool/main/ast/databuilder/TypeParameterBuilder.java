package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import java.util.*;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.TypeParameterStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedCallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedUnitInfo;


/**
 * �^�p�����[�^�����\�z����r���_�[
 * 
 * @author kou-tngt, t-miyake
 *
 */
public class TypeParameterBuilder extends CompoundDataBuilder<UnresolvedTypeParameterInfo> {

    /**
     * �����ɗ^����ꂽ�\�z�f�[�^�̊Ǘ��҂ƃf�t�H���g�̖��O���r���_�[�C�^���r���_�[��p���ď���������
     * @param buildDataManager�@�\�z�f�[�^�̊Ǘ���
     */
    public TypeParameterBuilder(final BuildDataManager buildDataManager) {
        this(buildDataManager, new NameBuilder(), new TypeBuilder(buildDataManager));
    }

    /**
     * �����ɗ^����ꂽ�\�z�f�[�^�̊Ǘ��ҁC���O���r���_�[�C�^���r���_�[��p���ď���������
     * @param buildDataManager�@�\�z�f�[�^�̊Ǘ���
     * @param nameBuilder�@���O���r���_�[
     * @param typeBuilder�@�^���r���_�[
     */
    public TypeParameterBuilder(final BuildDataManager buildDataManager,
            final NameBuilder nameBuilder, final TypeBuilder typeBuilder) {
        if (null == buildDataManager) {
            throw new NullPointerException("buildDataManager is null.");
        }

        if (null == nameBuilder) {
            throw new NullPointerException("nameBuilder is null.");
        }

        if (null == typeBuilder) {
            throw new NullPointerException("typeBuilder is null.");
        }

        this.buildDataManager = buildDataManager;
        this.nameBuilder = nameBuilder;
        this.typeBuilder = typeBuilder;

        //�����r���_�[�̓o�^
        this.addInnerBuilder(nameBuilder);
        this.addInnerBuilder(typeBuilder);

        //��Ԓʒm���󂯎�肽�����̂�o�^
        this.addStateManager(typeParameterStateManager);
    }

    /**
     * ��ԕω��C�x���g�̒ʒm���󂯂郁�\�b�h�D
     * @param event ��ԕω��C�x���g
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.StateDrivenDataBuilder#stateChanged(jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent)
     */
    @Override
    public void stateChanged(final StateChangeEvent<AstVisitEvent> event) {
        final StateChangeEventType type = event.getType();

        if (this.isActive()) {
            if (type.equals(TypeParameterStateManager.TYPE_PARAMETER.ENTER_TYPE_PARAMETER_DEF)) {
                //�^�p�����[�^��`�ɓ������̂ł���΂�
                this.nameBuilder.activate();
                this.typeBuilder.activate();
                this.inTypeParameterDefinition = true;
            } else if (type
                    .equals(TypeParameterStateManager.TYPE_PARAMETER.EXIT_TYPE_PARAMETER_DEF)) {
                //�^�p�����[�^��`���I������̂ŁC�f�[�^���\�z���Č�n��
                this.buildTypeParameter();

                this.nameBuilder.deactivate();
                this.typeBuilder.deactivate();
                this.lowerBoundsType = null;
                this.upperBoundsType.clear();
                this.nameBuilder.clearBuiltData();
                this.typeBuilder.clearBuiltData();
                this.inTypeParameterDefinition = false;

            } else if (this.inTypeParameterDefinition) {
                //�^�p�����[�^��`���Ȃ��ł̏o����
                /* (�^�ϐ�)Type Variable�ɂ�"Super"�̃g�[�N���͕��@�㗈�Ȃ��̂ō폜
                if (type.equals(TypeParameterStateManager.TYPE_PARAMETER.ENTER_TYPE_LOWER_BOUNDS)) {
                    //�^�̉����錾�������̂Ō^�\�z��������΂�
                    this.nameBuilder.deactivate();
                    this.typeBuilder.activate();
                } else if (type
                        .equals(TypeParameterStateManager.TYPE_PARAMETER.EXIT_TYPE_LOWER_BOUNDS)) {
                    //�^�̉��������\�z����
                    this.lowerBoundsType = this.builtTypeBounds(); 
                } else */
                if (type.equals(TypeParameterStateManager.TYPE_PARAMETER.ENTER_TYPE_UPPER_BOUNDS)) {
                    //�^�̏���錾�������̂ō\�z��������΂�
                    this.nameBuilder.deactivate();
                    this.typeBuilder.activate();
                } else if (type
                        .equals(TypeParameterStateManager.TYPE_PARAMETER.EXIT_TYPE_UPPER_BOUNDS)) {
                    //�^�̏�������\�z����
                    UnresolvedTypeInfo<?> typeInfo = this.builtTypeBounds();
                    if (this.typeParameterStateManager.isInTypeParameterDefinition()) {
                        this.upperBoundsType.add(typeInfo);
                    }
                } else if (type
                        .equals(TypeParameterStateManager.TYPE_PARAMETER.ENTER_TYPE_ADDITIONAL_BOUNDS)) {

                } else if (type
                        .equals(TypeParameterStateManager.TYPE_PARAMETER.EXIT_TYPE_ADDITIONAL_BOUNDS)) {
                    UnresolvedTypeInfo<?> typeInfo = this.builtTypeBounds();
                    this.upperBoundsType.add(typeInfo);
                }
            }
        }
    }

    /**
     * �^�p�����[�^��`���̏I�����ɌĂяo����C�^�p�����[�^���\�z���郁�\�b�h
     * ���̃��\�b�h���I�[�o�[���C�h���邱�ƂŁC�^�p�����[�^��`���ŔC�ӂ̏������s�킹�邱�Ƃ��ł���D
     */
    protected void buildTypeParameter() {
        //�^�p�����[�^�̖��O�������������炨������
        assert (this.nameBuilder.getBuiltDataCount() == 1);

        final String[] name = this.nameBuilder.getFirstBuiltData();

        //�^�p�����[�^�̖��O�������ɕ�����ĂĂ���������
        assert (name.length == 1);

        final UnresolvedUnitInfo<? extends UnitInfo> ownerUnit = this.buildDataManager
                .getCurrentUnit();

        assert (ownerUnit instanceof UnresolvedCallableUnitInfo)
                || (ownerUnit instanceof UnresolvedClassInfo) : "Illegal state: not parametrized unit";

        //�^�̏����񉺌������擾
        final List<UnresolvedTypeInfo<? extends TypeInfo>> upperBounds = this.getUpperBounds();
        final UnresolvedTypeInfo<? extends TypeInfo> lowerBounds = this.getLowerBounds();
        final int index = buildDataManager.getCurrentTypeParameterCount();

        UnresolvedTypeParameterInfo parameter = null;

        if (null == upperBounds || upperBounds.isEmpty() || checkUpperBounds()) {
            List<UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo>> upperReferenceBounds = new ArrayList<UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo>>();
            for (final UnresolvedTypeInfo<? extends TypeInfo> typeInfo : this.getUpperBounds()) {
                upperReferenceBounds.add((UnresolvedReferenceTypeInfo<?>) typeInfo);
            }
            parameter = new UnresolvedTypeParameterInfo(ownerUnit, name[0], index,
                    upperReferenceBounds);
            //            else {
            //                //����������ꍇ�͂����������
            //                parameter = new UnresolvedSuperTypeParameterInfo(ownerUnit, name[0], index,
            //                        (UnresolvedReferenceTypeInfo<?>) upperBounds,
            //                        (UnresolvedReferenceTypeInfo<?>) lowerBounds);
            //            }
        } else {
            // ���Ȃ��Ƃ�Java�ł͂����ɓ��B���Ă͂����Ȃ�(�^�p�����[�^�͎Q�ƌ^������`�ł��Ȃ�)
            // TODO C#�̏ꍇ�͌^�p�����[�^���v���~�e�B�u�^�̏ꍇ������̂őΏ����K�v
            assert false : "Illegal state: type parameter is not reference type";
        }
        //�Ō�Ƀf�[�^�Ǘ��҂ɓo�^����
        this.buildDataManager.addTypeParameger(parameter);
    }

    /**
     * upperBounds�Ɋi�[����Ă���TypeInfo���S��ReferenceTypeInfo���ǂ����𒲂ׂ�
     * @return upperBounds�Ɋi�[����Ă���TypeInfo���S��ReferenceTypeInfo�Ȃ�true
     */
    private boolean checkUpperBounds() {
        for (final UnresolvedTypeInfo<?> typeInfo : this.getUpperBounds()) {
            if (!(typeInfo instanceof UnresolvedReferenceTypeInfo<?>)) {
                return false;
            }
        }
        return true;

    }

    /**
     * �^�̏������Ԃ��D
     * @return�@�^�̏�����
     */
    protected List<UnresolvedTypeInfo<? extends TypeInfo>> getUpperBounds() {
        return this.upperBoundsType;
    }

    /**
     * �^�̉�������Ԃ��D
     * @return�@�^�̉������
     */
    protected UnresolvedTypeInfo<? extends TypeInfo> getLowerBounds() {
        return this.lowerBoundsType;
    }

    /**
     * �Ō�ɍ\�z���ꂽ�^�̏���Ԃ��D
     * @return�@�Ō�ɍ\�z���ꂽ�^
     */
    protected UnresolvedTypeInfo<? extends TypeInfo> builtTypeBounds() {
        return this.typeBuilder.popLastBuiltData();
    }

    /**
     * ���O�\�z���s���r���_�[��Ԃ��D
     * @return�@���O�\�z���s���r���_�[
     */
    protected NameBuilder getNameBuilder() {
        return this.nameBuilder;
    }

    /**
     * �^�����\�z����r���_�[��Ԃ�
     * @return�@�^�����\�z����r���_�[
     */
    protected TypeBuilder getTypeBuilder() {
        return this.typeBuilder;
    }

    /**
     * ���O�\�z���s���r���_�[
     */
    private final NameBuilder nameBuilder;

    /**
     * �^�����\�z����r���_�[
     */
    private final TypeBuilder typeBuilder;

    /**
     * �\�z���̊Ǘ���
     */
    private final BuildDataManager buildDataManager;

    /**
     * �^�p�����[�^�̏��
     * ����͕����w��ł���̂Ń��X�g�ŊǗ�
     */
    private List<UnresolvedTypeInfo<? extends TypeInfo>> upperBoundsType = new ArrayList<UnresolvedTypeInfo<? extends TypeInfo>>();

    /**
     * �^�p�����[�^�̉���
     */
    private UnresolvedTypeInfo<? extends TypeInfo> lowerBoundsType;

    /**
     * �^�p�����[�^��`���ɂ��邩�ǂ�����\��
     */
    private boolean inTypeParameterDefinition = false;

    private TypeParameterStateManager typeParameterStateManager = new TypeParameterStateManager();
}
