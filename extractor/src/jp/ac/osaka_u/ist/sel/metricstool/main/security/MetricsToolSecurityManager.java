package jp.ac.osaka_u.ist.sel.metricstool.main.security;


import java.security.AccessControlException;
import java.security.Permission;
import java.security.Permissions;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.WeakHashSet;


/**
 * ���g���N�X�c�[���̃A�N�Z�X������X���b�h�P�ʂœ��I�ɍs���Z�L�����e�B�}�l�[�W��
 * <p>
 * �ŏ��� {@link #getInstance()} ���Ă񂾃X���b�h�ɑS�Ẵp�[�~�b�V��������������ʌ�����^����D
 * ���̌�C���ʌ����������Ă���X���b�h�� {@link #addPrivilegeThread(Thread)} ���\�b�h��ʂ��ēo�^�����X���b�h�ɂ����l�̓��ʌ�����^����D
 * ���ʌ�����^����ꂽ�X���b�h�̓��ʌ����͍폜����Ȃ��D
 * <p>
 * ���̃N���X�̗��p�҂͓��ʌ����������Ȃ��X���b�h����̃A�N�Z�X��r���������ꍇ�� {@link #checkAccess()} ���\�b�h���ĂԁD
 * �Ăяo�����X���b�h�����ʌ����������Ȃ��ꍇ�́C {@link AccessControlException}�@��O���X���[�����D
 * ���ʌ��������X���b�h�ł������ꍇ�́C���������ɏ�����Ԃ��D
 * <p>
 * �v���O�C������̃A�N�Z�X��r���������ꍇ�́C {@link #checkPlugin()} ���\�b�h���Ă�.
 * �Ăяo�����v���O�C�����o�^���ꂽ�v���O�C���X���b�h�Ɠ����O���[�v�ɑ����Ă���΁C {@link AccessControlException}���X���[�����.
 * �v���O�C���X���b�h�Ɠ����O���[�v�łȂ���΁C���������ɏ�����Ԃ�.
 * <p>
 * �܂��C�ʂ̃A�N�Z�X�R���g���[���Ƃ��ăO���[�o���p�[�~�b�V�����Ƃ����T�O�������D
 * �O���[�o���p�[�~�b�V�����Ƃ̓v���O�C����GUI���܂�VM��̑S�ẴN���X�ɋ������p�[�~�b�V�����̂��ƂŁC
 * {@link #addGlobalPermission(Permission)}�ɂ���ēo�^���ꂽ�p�[�~�b�V�����́C
 * �S�ẴX���b�h�C�S�ẴR���e�L�X�g�C�S�ẴR�[�h�\�[�X�ɋ������D
 * �������C�O���[�o���p�[�~�b�V�����̒ǉ��͓��ʌ����X���b�h�݂̂���\�ł���D
 * <p>
 * �e�X���b�h�P�ʂ̓���̃p�[�~�b�V�����������邱�Ƃ��ł���.
 * {@link #addThreadPermission(Thread, Permission)}���\�b�h�ɂ���āC�C�ӂ̃X���b�h�ɔC�ӂ̃p�[�~�b�V�����������邱�Ƃ��ł���.
 * �������C���̃��\�b�h�͓��ʌ��������X���b�h�݂̂���Ăяo�����Ƃ��ł���.
 * <p>
 * ���ʌ����������Ȃ��X���b�h�ŁC�p�[�~�b�V�����o�^����Ă��Ȃ����̂ɂ��ẮC
 * �ʏ�� {@link SecurityManager} �Ɠ����̋@�\��K�p����D
 * <p>
 * �V���O���g���N���X�ł��邽�߁C�R���X�g���N�^�� private �ł���C���̃N���X���p�����邱�Ƃ͂ł��Ȃ����C
 * ����𖾎��I�ɐ錾���邽�߂� final �C���q�����Ă���D
 * 
 * @author kou-tngt
 *
 */
public final class MetricsToolSecurityManager extends SecurityManager {

    /**
     * �V���O���g���C���X�^���X���擾����
     * @return �V���O���g���C���X�^���X
     */
    public static MetricsToolSecurityManager getInstance() {
        if (null == SINGLETON) {
            synchronized (MetricsToolSecurityManager.class) {
                if (null == SINGLETON) {
                    SINGLETON = new MetricsToolSecurityManager();
                }
            }
        }
        return SINGLETON;
    }

    /**
     * ���g���N�X�c�[�������s���Ă���VM��̑S�̂ŋ�����p�[�~�b�V������ǉ�����D
     * ���M���O�Ƃ���肽���ꍇ�́C������g���ēo�^����D
     * �o�^����ɂ͌Ăяo���X���b�h�ɓ��ʌ������K�v
     * @param permission ���������p�[�~�b�V�����C���X�^���X
     * @throws AccessControlException �X���b�h�ɓ��ʌ������Ȃ��ꍇ
     * @throws NullPointerException permission��null�̏ꍇ
     */
    public final void addGlobalPermission(final Permission permission) {
        this.checkAccess();

        if (null == permission) {
            throw new NullPointerException("permission is null.");
        }

        this.globalPermissions.add(permission);
    }

    /**
     * �v���O�C���X���b�h��o�^����.
     * �o�^���ꂽ�X���b�h�Ɠ����X���b�h�O���[�v�ɑ�����X���b�h���C�v���O�C���X���b�h�Ɣ��肳���
     * @param thread �o�^����v���O�C���X���b�h
     * @throws NullPointerException�@thread��null�̏ꍇ
     */
    public final void addPluginThread(final Thread thread) {
        if (null == thread) {
            throw new NullPointerException("group is null.");
        }

        this.pluginThreadGroups.add(thread.getThreadGroup());
    }

    /**
     * ���� thread �ŗ^����ꂽ�X���b�h�ɓ��ʌ�����t�^����
     * @param thread ���ʌ�����t�^�������X���b�h
     * @throws AccessControlException �Ăяo�����̃X���b�h�����ʌ����������Ă��Ȃ������ꍇ
     * @throws NullPointerException thread��null�������ꍇ
     */
    public final void addPrivilegeThread(final Thread thread) {
        this.checkAccess();
        if (null == thread) {
            throw new NullPointerException("Added thread is null.");
        }

        this.privilegeThreads.add(thread);
    }

    /**
     * �X���b�h�ʂɋ�����p�[�~�b�V������ݒ肷��.
     * ���̃��\�b�h�Őݒ肳�ꂽ�p�[�~�b�V�����́C�����ŗ^����ꂽ�X���b�h�݂̂ŗL���ł���C
     * ���̃X���b�h����쐬���ꂽ�ʂ̃X���b�h�ɂ͏��n����Ȃ�.
     * @param thread �p�[�~�b�V������������X���b�h
     * @param permission ������p�[�~�b�V����
     * @throws AccessControlException �Ăяo�����̃X���b�h�����ʌ����������Ȃ��ꍇ
     * @throws NullPointerException permission��null�̏ꍇ
     */
    public final void addThreadPermission(final Thread thread, final Permission permission) {
        this.checkAccess();
        if (null == thread) {
            throw new NullPointerException("thread is null.");
        }
        if (null == permission) {
            throw new NullPointerException("permission is null.");
        }

        Permissions permissions;
        if (this.threadPermissions.containsKey(thread)) {
            permissions = this.threadPermissions.get(thread);
        } else {
            permissions = new Permissions();
            this.threadPermissions.put(thread, permissions);
        }
        permissions.add(permission);
    }

    /**
     * ���ʌ����X���b�h����̌Ăяo�����ǂ����𔻒肷�郁�\�b�h�D
     * ���ʌ����X���b�h�ȊO����Ăяo�����ƁC {@link AccessControlException}�@���X���[����D
     * @throws AccessControlException ���ʌ����X���b�h�ȊO����Ăяo���ꂽ�ꍇ
     */
    public final void checkAccess() {
        //�J�����g�X���b�h���擾
        final Thread currentThread = Thread.currentThread();
        if (!this.isPrivilegeThread(currentThread)) {
            //�o�^����Ă��Ȃ�����

            //�G���[�\���p�ɃX�^�b�N�ƃ��[�X�̎擾
            final StackTraceElement[] traces = currentThread.getStackTrace();

            //���̃��\�b�h�̌Ăяo�����̃��\�b�h���擾
            assert (null != traces && 3 < traces.length) : "Illegal state: empty stack trace.";
            final StackTraceElement callerMethod = traces[3];

            throw new AccessControlException(
                    "Permission denide: current thread can not invoke the method "
                            + callerMethod.getClassName() + "#" + callerMethod.getMethodName()
                            + ".");
        }
    }

    /**
     * �v���O�C���X���b�h����̌Ăяo�����ǂ����𔻒肷�郁�\�b�h.
     * �v���O�C���X���b�h����̌Ăяo���ł���΁C{@link AccessControlException}�@���X���[����D
     * @throws AccessControlException �v���O�C���X���b�h����Ăяo���ꂽ�ꍇ
     */
    public final void checkPlugin() {
        //�J�����g�X���b�h���擾
        final Thread currentThread = Thread.currentThread();
        if (this.isPluginThread(currentThread)) {
            //�v���O�C���X���b�h������

            //�G���[�\���p�ɃX�^�b�N�ƃ��[�X�̎擾
            final StackTraceElement[] traces = currentThread.getStackTrace();

            //���̃��\�b�h�̌Ăяo�����̃��\�b�h���擾
            assert (null != traces && 3 < traces.length) : "Illegal state: empty stack trace.";
            final StackTraceElement callerMethod = traces[3];

            throw new AccessControlException(
                    "Permission denide: current thread can not invoke the method "
                            + callerMethod.getClassName() + "#" + callerMethod.getMethodName()
                            + ".");
        }
    }

    /**
     * {@link SecurityManager#checkPermission(Permission)} ���\�b�h���I�[�o�[���C�h���C
     * ���ʌ����X���b�h����̌Ăяo���C�O���[�o���p�[�~�b�V�����Ƃ��ēo�^�ς݁C�X���b�h�ʂ̃p�[�~�b�V�����Ƃ��ēo�^�ς݂ł���΁C
     * �p�[�~�b�V�����`�F�b�N�������ɏI������D
     * �����łȂ��Ȃ�C�e�N���X�̃��\�b�h���ĂсC�p�[�~�b�V�����̃`�F�b�N���s���D
     * @param perm �`�F�b�N����p�[�~�b�V����
     * @throws NullPointerException ����perm��null�̏ꍇ
     * @throws SecurityException �p�[�~�b�V������������Ă��Ȃ��ꍇ
     * @see java.lang.SecurityManager#checkPermission(java.security.Permission)
     */
    @Override
    public final void checkPermission(final Permission perm) {
        if (null == perm) {
            throw new NullPointerException("Permission is null.");
        }

        if (this.isPrivilegeThread()) {
            return;
        } else if (this.globalPermissions.implies(perm)) {
            return;
        } else {
            final Thread current = Thread.currentThread();

            //���̃��\�b�h������Ăяo����āC�������������Ă���X���b�h�O���[�v�̐e�O���[�v��null���ǂ����𒲂ׂɗ����X���b�h����������
            if (perm.getName().equals("modifyThreadGroup")
                    && this.groupParentCheckingThread.contains(current)) {
                return;
            }

            //�������������Ă���X���b�h�O���[�v�̐e�O���[�v��null���ǂ����𒲂ׂɂ���
            //null�Ȃ�V�X�e���X���b�h�ł���D
            this.groupParentCheckingThread.add(current);
            boolean isSystemThread = null == current.getThreadGroup().getParent();
            this.groupParentCheckingThread.remove(current);

            if (isSystemThread) {
                //�V�X�e���X���b�h�̏ꍇ�͑S�Ă������Ă��܂��D
                return;
            } else if (this.threadPermissions.containsKey(current)) {
                final Permissions permissions = this.threadPermissions.get(current);
                if (permissions.implies(perm)) {
                    return;
                }
            }

            super.checkPermission(perm);
        }
    }

    /**
     * {@link SecurityManager#checkPermission(Permission, Object)} ���\�b�h���I�[�o�[���C�h���C
     * �O���[�o���p�[�~�b�V�����Ƃ��ēo�^�ς݂ł���΃p�[�~�b�V�����`�F�b�N�������ɏI������D
     * �����łȂ��Ȃ�C�e�N���X�̃��\�b�h���ĂсC�p�[�~�b�V�����̃`�F�b�N���s���D
     * @param perm �`�F�b�N����p�[�~�b�V����
     * @throws NullPointerException ����perm��null�̏ꍇ
     * @throws SecurityException perm���O���[�o���p�[�~�b�V�����łȂ��ꍇ�ɁC�p�[�~�b�V������������Ă��Ȃ��ꍇ
     * @see java.lang.SecurityManager#checkPermission(Permission, Object)
     */
    @Override
    public void checkPermission(final Permission perm, final Object context) {
        if (null == perm) {
            throw new NullPointerException("Permission is null.");
        }
        if (this.globalPermissions.implies(perm)) {
            return;
        } else {
            super.checkPermission(perm, context);
        }
    }

    /**
     * �J�����g�X���b�h���v���O�C���X���b�h���ǂ�����Ԃ�
     * @return �J�����g�X���b�h���v���O�C���X���b�h�Ȃ�true�C�����łȂ��Ȃ�false
     */
    public final boolean isPluginThread() {
        return this.isPluginThread(Thread.currentThread());
    }

    /**
     * �����̃X���b�h���v���O�C���X���b�h���ǂ�����Ԃ�
     * @param thread ���ׂ����X���b�h
     * @return �v���O�C���X���b�h�Ȃ�true�C�����łȂ��Ȃ�false
     */
    public final boolean isPluginThread(final Thread thread) {
        return this.pluginThreadGroups.contains(thread.getThreadGroup());
    }

    /**
     * �J�����g�X���b�h�����ʌ����������Ă��邩��Ԃ�
     * @return ���ʌ����������Ă����true
     */
    public final boolean isPrivilegeThread() {
        return this.isPrivilegeThread(Thread.currentThread());
    }

    /**
     * ���� thread �ŗ^����ꂽ�X���b�h�����ʌ����������Ă��邩��Ԃ�
     * @param thread ���ʌ����������Ă��邩�𒲂ׂ����X���b�h
     * @return ���� thread �ŗ^����ꂽ�X���b�h�����ʌ����������Ă����true
     */
    public final boolean isPrivilegeThread(final Thread thread) {
        return this.privilegeThreads.contains(thread);
    }

    /**
     * �v���O�C���̃A�N�Z�X��������������
     * @param plugin
     */
    public final void removePluginPermission(final AbstractPlugin plugin) {
        Thread current = Thread.currentThread();

        //������Ă����p�[�~�b�V����������Ă���.
        Permissions permissions;
        if (this.threadPermissions.containsKey(current)) {
            permissions = this.threadPermissions.get(current);
        } else {
            permissions = new Permissions();
            this.threadPermissions.put(current, permissions);
        }

        //�V�K�p�[�~�b�V�����Z�b�g�����
        Permissions newPermissions = new Permissions();

        //������Ă����p�[�~�b�V�������v���O�C���̃p�[�~�b�V�����Z�b�g�Ɋ܂܂�ċ��Ȃ���ΐV�K�p�[�~�b�V�����Z�b�g�ɓ����
        for (Enumeration<Permission> enumerator = permissions.elements(); enumerator
                .hasMoreElements();) {
            Permission permission = enumerator.nextElement();
            boolean include = false;
            for (Enumeration<Permission> pluginPermissions = plugin.getPermissions().elements(); pluginPermissions
                    .hasMoreElements();) {
                Permission pluginPermission = pluginPermissions.nextElement();
                if (pluginPermission == permission) {//�C���X�^���X�̔�r������
                    include = true;
                    break;
                }
            }
            if (!include) {
                newPermissions.add(permission);
            }
        }
        //�V�K�p�[�~�b�V�����Z�b�g�����̃X���b�h�̃p�[�~�b�V�����Ƃ��ăZ�b�g����
        this.threadPermissions.put(current, newPermissions);
    }

    /**
     * �v���O�C���̃A�N�Z�X������ݒ肷��
     * @param plugin�@�v���O�C���C���X�^���X
     */
    public final void requestPluginPermission(final AbstractPlugin plugin) {
        Thread current = Thread.currentThread();

        Permissions permissions;
        if (this.threadPermissions.containsKey(current)) {
            permissions = this.threadPermissions.get(current);
        } else {
            permissions = new Permissions();
            this.threadPermissions.put(current, permissions);
        }

        Permissions pluginPermissions = plugin.getPermissions();

        for (Enumeration<Permission> enumeration = pluginPermissions.elements(); enumeration
                .hasMoreElements();) {
            permissions.add(enumeration.nextElement());
        }
    }

    /**
     * �V���O���g���pprivate�R���X�g���N�^�D
     * �������Ăяo�����X���b�h���������ʌ����N���X�Ƃ��ēo�^����D
     */
    private MetricsToolSecurityManager() {
        final Thread thread = Thread.currentThread();
        assert (null != thread) : "Illegal state : current thread is null.";
        this.privilegeThreads.add(thread);
    }

    /**
     * ���ʌ����X���b�h�̃Z�b�g�D
     * ���̑S�Ă̎Q�Ƃ��؂ꂽ����ʌ����X���b�h�������Ă��Ă��Ӗ����Ȃ��̂ŁC��Q�Ƃɂ��邽�߂� {@link WeakHashSet} ��p����D
     * �܂��C�}���`�X���b�h���œK�؂ɓ��삳���邽�߂� {@link Collections#synchronizedSet(Set)} ���g���ē���������D
     */
    private final Set<Thread> privilegeThreads = Collections
            .synchronizedSet((new WeakHashSet<Thread>()));

    /**
     * �v���O�C���̃X���b�h�O���[�v�̃Z�b�g.
     * 
     */
    private final Set<ThreadGroup> pluginThreadGroups = Collections
            .synchronizedSet(new WeakHashSet<ThreadGroup>());

    /**
     * {@link #checkPermission(Permission)}���\�b�h�ɂ����ė��p����� {@link ThreadGroup#getParent()} ���\�b�h�̌Ăяo����
     * ���s�����X���b�h���ꎞ�I�ɕۑ����Ă������߂̃Z�b�g�D
     */
    private final Set<Thread> groupParentCheckingThread = new WeakHashSet<Thread>();

    /**
     * �V���O���g���C���X�^���X�D
     */
    private static MetricsToolSecurityManager SINGLETON;

    /**
     * VM�S�̂ŋ�����p�[�~�b�V����
     */
    private final Permissions globalPermissions = new Permissions();

    /**
     * �e�X���b�h���ɕt�^�����p�[�~�b�V�����̃}�b�v
     */
    private final Map<Thread, Permissions> threadPermissions = new WeakHashMap<Thread, Permissions>();
}
