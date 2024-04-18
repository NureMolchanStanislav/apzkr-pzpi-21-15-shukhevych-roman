using Application.IRepositories.Base;
using Domain.Entities;

namespace Application.IRepositories;

public interface IBrandBonusesRepository : IBaseRepository<BrandBonus>
{
    Task<BrandBonus> UpdateAsync(BrandBonus brandBonus, CancellationToken cancellationToken);
}