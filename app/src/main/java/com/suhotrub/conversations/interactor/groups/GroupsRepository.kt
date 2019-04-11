package com.suhotrub.conversations.interactor.groups

import com.suhotrub.conversations.model.group.GroupCreateDto
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

    fun joinGroup(groupName: String) =
            groupsApi.join(groupName)

    fun getJoinedGroups(offset: Int) =
            groupsApi.getJoinedGroups(20, offset)

    fun getCreatedGroups(offset: Int) =
            groupsApi.getCreatedGroups(20, offset)




    fun getGroupParticipants(groupGuid: String, offset: Int) =
            groupsApi.getGroupParticicpants(groupGuid, 200, offset)
}