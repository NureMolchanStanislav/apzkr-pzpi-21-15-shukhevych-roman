using Application.IRepositories.Base;
using Application.Models.StatisticsDtos;
using Domain.Entities;

namespace Application.IRepositories;

public interface IUsageHistoryRepository : IBaseRepository<UsageHistory>
{
    Task<List<UsageHistory>> GetAllAsync(CancellationToken cancellationToken = default);
}