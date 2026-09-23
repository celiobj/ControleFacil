"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.ChecklistsController = void 0;
const common_1 = require("@nestjs/common");
const swagger_1 = require("@nestjs/swagger");
const class_validator_1 = require("class-validator");
const client_1 = require("@prisma/client");
const jwt_guard_1 = require("../auth/jwt.guard");
const checklists_service_1 = require("./checklists.service");
class ChecklistItemUpdateDto {
    status;
    responsible;
    dueDate;
    notes;
}
__decorate([
    (0, class_validator_1.IsOptional)(),
    (0, class_validator_1.IsEnum)(client_1.ChecklistItemStatus),
    __metadata("design:type", String)
], ChecklistItemUpdateDto.prototype, "status", void 0);
__decorate([
    (0, class_validator_1.IsOptional)(),
    (0, class_validator_1.IsString)(),
    __metadata("design:type", String)
], ChecklistItemUpdateDto.prototype, "responsible", void 0);
__decorate([
    (0, class_validator_1.IsOptional)(),
    (0, class_validator_1.IsDateString)(),
    __metadata("design:type", String)
], ChecklistItemUpdateDto.prototype, "dueDate", void 0);
__decorate([
    (0, class_validator_1.IsOptional)(),
    (0, class_validator_1.IsString)(),
    __metadata("design:type", String)
], ChecklistItemUpdateDto.prototype, "notes", void 0);
class ChecklistItemCreateDto {
    phase;
    stage;
    task;
    responsible;
    dueDate;
    notes;
}
__decorate([
    (0, class_validator_1.IsString)(),
    (0, class_validator_1.IsNotEmpty)(),
    __metadata("design:type", String)
], ChecklistItemCreateDto.prototype, "phase", void 0);
__decorate([
    (0, class_validator_1.IsString)(),
    (0, class_validator_1.IsNotEmpty)(),
    __metadata("design:type", String)
], ChecklistItemCreateDto.prototype, "stage", void 0);
__decorate([
    (0, class_validator_1.IsString)(),
    (0, class_validator_1.IsNotEmpty)(),
    __metadata("design:type", String)
], ChecklistItemCreateDto.prototype, "task", void 0);
__decorate([
    (0, class_validator_1.IsOptional)(),
    (0, class_validator_1.IsString)(),
    __metadata("design:type", String)
], ChecklistItemCreateDto.prototype, "responsible", void 0);
__decorate([
    (0, class_validator_1.IsOptional)(),
    (0, class_validator_1.IsDateString)(),
    __metadata("design:type", String)
], ChecklistItemCreateDto.prototype, "dueDate", void 0);
__decorate([
    (0, class_validator_1.IsOptional)(),
    (0, class_validator_1.IsString)(),
    __metadata("design:type", String)
], ChecklistItemCreateDto.prototype, "notes", void 0);
let ChecklistsController = class ChecklistsController {
    service;
    constructor(service) {
        this.service = service;
    }
    get(propertyId) {
        return this.service.ensure(propertyId);
    }
    add(propertyId, dto) {
        return this.service.addItem(propertyId, dto);
    }
    update(itemId, dto) {
        return this.service.updateItem(itemId, dto);
    }
};
exports.ChecklistsController = ChecklistsController;
__decorate([
    (0, common_1.Get)("properties/:propertyId/checklist"),
    __param(0, (0, common_1.Param)("propertyId")),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [String]),
    __metadata("design:returntype", void 0)
], ChecklistsController.prototype, "get", null);
__decorate([
    (0, common_1.Post)("properties/:propertyId/checklist/items"),
    __param(0, (0, common_1.Param)("propertyId")),
    __param(1, (0, common_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [String, ChecklistItemCreateDto]),
    __metadata("design:returntype", void 0)
], ChecklistsController.prototype, "add", null);
__decorate([
    (0, common_1.Patch)("checklist/items/:itemId"),
    __param(0, (0, common_1.Param)("itemId")),
    __param(1, (0, common_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [String, ChecklistItemUpdateDto]),
    __metadata("design:returntype", void 0)
], ChecklistsController.prototype, "update", null);
exports.ChecklistsController = ChecklistsController = __decorate([
    (0, swagger_1.ApiTags)("checklists"),
    (0, swagger_1.ApiBearerAuth)(),
    (0, common_1.UseGuards)(jwt_guard_1.JwtGuard),
    (0, common_1.Controller)(),
    __param(0, (0, common_1.Inject)(checklists_service_1.ChecklistsService)),
    __metadata("design:paramtypes", [checklists_service_1.ChecklistsService])
], ChecklistsController);
//# sourceMappingURL=checklists.controller.js.map