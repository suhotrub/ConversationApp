package com.suhotrub.conversations.interactor.groups

import com.suhotrub.conversations.model.group.GroupCreateDto
import com.suhotrub.conversations.model.group.GroupInviteDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupsRepository @Inject constructor(
        val groupsApi: GroupsApi
) {


    fun createGroup(
            name: String,
            description: String?,
            avatarLink: String,
            users:List<String>
    ) = groupsApi.create(
            GroupCreateDto(
                    name,
                    users,
                    description,
                    avatarLink
            )
    )

    fun inviteToGroup(groupGuid: String, userGuid:String) =
            groupsApi.invite(GroupInviteDto(userGuid,groupGuid))

    fun leaveGroup(groupGuid: String) =
            groupsApi.leave(groupGuid)

    fun getJoinedGroups(offset: Int) =
            groupsApi.getJoinedGroups(20, offset)

    fun getGroupParticipants(groupGuid: String, offset: Int) =
            groupsApi.getGroupParticicpants(groupGuid, 200, offset)
}