using Application.IRepositories.Base;
using Application.Models.UpdateDtos;
using Domain.Entities;

namespace Application.IRepositories;

public interface IRFIDTagsRepository : IBaseRepository<RFIDTag>
{
    Task<bool> UpdateStatus(string tagId, CancellationToken cancellationToken);

    Task<bool> GetStatus(string sensorId, CancellationToken cancellationToken);
}